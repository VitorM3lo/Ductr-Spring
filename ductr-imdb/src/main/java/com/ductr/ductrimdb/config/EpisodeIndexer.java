package com.ductr.ductrimdb.config;

import java.io.File;

import com.ductr.ductrentity.entities.Episode;
import com.ductr.ductrimdb.entity.EpisodeData;
import com.ductr.ductrimdb.mapper.EpisodeMapper;
import com.ductr.ductrimdb.processor.EpisodeDataProcessor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class EpisodeIndexer extends IndexerBatchConfigurer {

  @Value("${file.repository}")
  private String folder;

  @Value("${episodes.dataset}")
  private String file;

  @Bean
  public Job episodeIndexerJob() {
    return jobBuilderFactory.get("episodeIndexerJob").listener(getJobListener(file)).start(indexEpisodes()).build();
  }

  @Bean
  public Step indexEpisodes() {
    return stepBuilderFactory.get("indexEpisodes").allowStartIfComplete(true)
        .transactionManager(getTransactionManager()).<EpisodeData, Episode>chunk(1000)
        .reader(episodeReader())
        .processor(getEpisodeProcessor()).writer(episodeWriter()).build();
  }

  @Bean
  @StepScope
  public FlatFileItemReader<EpisodeData> episodeReader() {
    FlatFileItemReader<EpisodeData> itemReader = new FlatFileItemReader<>();
    FileSystemResource resource = new FileSystemResource(new File(folder + file.replace(".gz", "")));
    DefaultLineMapper<EpisodeData> lineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_TAB);
    tokenizer.setNames(new String[] { "tconst", "parentTconst", "seasonNumber", "episodeNumber" });
    tokenizer.setQuoteCharacter('\0');
    lineMapper.setLineTokenizer(tokenizer);
    lineMapper.setFieldSetMapper(new EpisodeMapper());
    itemReader.setResource(resource);
    itemReader.setLineMapper(lineMapper);
    itemReader.setLinesToSkip(1);
    itemReader.setStrict(false);
    itemReader.open(new ExecutionContext());
    return itemReader;
  }

  @Bean
  public EpisodeDataProcessor getEpisodeProcessor() {
    return new EpisodeDataProcessor();
  }

  @Bean
  public JpaItemWriter<Episode> episodeWriter() {
    return new JpaItemWriterBuilder<Episode>().entityManagerFactory(emf).build();
  }

}
