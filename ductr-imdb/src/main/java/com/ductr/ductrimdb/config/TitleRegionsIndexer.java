package com.ductr.ductrimdb.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ductr.ductrentity.entities.Title;
import com.ductr.ductrimdb.entity.TitleRegionData;
import com.ductr.ductrimdb.mapper.TitleRegionsMapper;
import com.ductr.ductrimdb.processor.TitleRegionDataProcessor;
import com.ductr.ductrimdb.processor.TitleRegionProcessor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class TitleRegionsIndexer extends IndexerBatchConfigurer {

  @Value("${file.repository}")
  private String folder;

  @Value("${movie.dataset}")
  private String file;

  @Autowired
  @Qualifier("titleRegionListener")
  private JobExecutionListener executionListener;

  @Bean
  public Job titleRegionIndexerJob() {
    return jobBuilderFactory.get("titleRegionIndexerJob").listener(executionListener).start(indexTitleRegions())
        .build();
  }

  @Bean
  public Step indexTitleRegions() {
    return stepBuilderFactory.get("indexTitleRegions").allowStartIfComplete(true).transactionManager(getTransactionManager())
        .<TitleRegionData, Title>chunk(1000).reader(titleRegionsReader()).processor(titleRegionsCompositeProcessor())
        .writer(titleRegionsWriter()).build();
  }

  @Bean
  @StepScope
  public FlatFileItemReader<TitleRegionData> titleRegionsReader() {
    FlatFileItemReader<TitleRegionData> itemReader = new FlatFileItemReader<>();
    FileSystemResource resource = new FileSystemResource(new File(folder + file.replace(".gz", "")));
    DefaultLineMapper<TitleRegionData> lineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_TAB);
    tokenizer.setNames(new String[] { "titleId", "ordering", "title", "region", "language", "types", "attributes",
        "isOriginalTitle" });
    tokenizer.setQuoteCharacter('\0');
    lineMapper.setLineTokenizer(tokenizer);
    lineMapper.setFieldSetMapper(new TitleRegionsMapper());
    itemReader.setResource(resource);
    itemReader.setLineMapper(lineMapper);
    itemReader.setLinesToSkip(1);
    itemReader.setStrict(false);
    itemReader.open(new ExecutionContext());
    return itemReader;
  }

  @Bean
  public CompositeItemProcessor titleRegionsCompositeProcessor() {
    CompositeItemProcessor compositeItemProcessor = new CompositeItemProcessor<>();
    List<ItemProcessor> itemProcessors = new ArrayList<>();
    itemProcessors.add(getTitleRegionDataProcessor());
    itemProcessors.add(getTitleRegionProcessor());
    compositeItemProcessor.setDelegates(itemProcessors);
    return compositeItemProcessor;
  }

  @Bean
  public TitleRegionDataProcessor getTitleRegionDataProcessor() {
    return new TitleRegionDataProcessor();
  }

  @Bean
  public TitleRegionProcessor getTitleRegionProcessor() {
    return new TitleRegionProcessor();
  }

  @Bean
  public JpaItemWriter<Title> titleRegionsWriter() {
    return new JpaItemWriterBuilder<Title>().entityManagerFactory(emf).build();
  }

  @Value("${movie.dataset}")
  @Bean(name = "titleRegionListener")
  @Override
  public JobExecutionListener getJobListener(String file) {
    return super.getJobListener(file);
  }

}
