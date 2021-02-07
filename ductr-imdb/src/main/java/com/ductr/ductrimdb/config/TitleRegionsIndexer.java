package com.ductr.ductrimdb.config;

import java.io.File;

import javax.sql.DataSource;

import com.ductr.ductrentity.entities.Title;
import com.ductr.ductrentity.entities.TitleRegion;
import com.ductr.ductrimdb.mapper.TitleRegionsMapper;
import com.ductr.ductrimdb.processor.TitleRegionProcessor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.orm.jpa.JpaTransactionManager;

@Configuration
@EnableBatchProcessing
public class TitleRegionsIndexer extends IndexerBatchConfigurer {

  @Value("${file.repository}")
  private String folder;

  @Value("${movie.dataset}")
  private String file;

  @Autowired
  DataSource datasource;

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
    return stepBuilderFactory.get("indexTitleRegions").allowStartIfComplete(true)
        .transactionManager(this.getTransactionManager(datasource)).<TitleRegion, Title>chunk(2000).reader(titleRegionsReader())
        .processor(getTitleRegionProcessor()).writer(titleRegionsWriter()).build();
  }

  @Bean
  @StepScope
  public FlatFileItemReader<TitleRegion> titleRegionsReader() {
    FlatFileItemReader<TitleRegion> itemReader = new FlatFileItemReader<>();
    FileSystemResource resource = new FileSystemResource(new File(folder + file.replace(".gz", "")));
    DefaultLineMapper<TitleRegion> lineMapper = new DefaultLineMapper<>();
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

  @Primary
  @Bean("regionsJobTransactionManager")
  @Override
  public JpaTransactionManager getTransactionManager(DataSource dataSource) {
    final JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setDataSource(dataSource);
    return transactionManager;

  }

}
