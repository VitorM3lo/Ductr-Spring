package com.ductr.ductrimdb.config;

import java.io.File;

import com.ductr.ductrentity.entities.Title;
import com.ductr.ductrimdb.mapper.RatingMapper;
import com.ductr.ductrimdb.processor.RatingProcessor;

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

@EnableBatchProcessing
@Configuration
public class RatingIndexer extends IndexerBatchConfigurer {
  
  @Value("${file.repository}")
  private String folder;

  @Value("${ratings.dataset}")
  private String file;

  @Bean
  public Job ratingsIndexerJob() {
    return jobBuilderFactory.get("ratingsIndexerJob").listener(getJobListener(file)).start(indexRatings()).build();
  }

  @Bean
  public Step indexRatings() {
    return stepBuilderFactory.get("indexRatings").transactionManager(getTransactionManager()).<Title, Title>chunk(1000)
        .reader(ratingReader()).processor(ratingProcessor()).writer(ratingWriter()).build();
  }

  @Bean
  @StepScope
  public FlatFileItemReader<Title> ratingReader() {
    FlatFileItemReader<Title> itemReader = new FlatFileItemReader<>();
    FileSystemResource resource = new FileSystemResource(new File(folder + file.replace(".gz", "")));
    DefaultLineMapper<Title> lineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_TAB);
    tokenizer.setNames(new String[] { "tconst", "averageRating", "numVotes" });
    tokenizer.setQuoteCharacter('\0');
    lineMapper.setLineTokenizer(tokenizer);
    lineMapper.setFieldSetMapper(new RatingMapper());
    itemReader.setResource(resource);
    itemReader.setLineMapper(lineMapper);
    itemReader.setLinesToSkip(1);
    itemReader.setStrict(false);
    itemReader.open(new ExecutionContext());
    return itemReader;
  }

  @Bean
  public RatingProcessor ratingProcessor() {
    return new RatingProcessor();
  }

  @Bean
  public JpaItemWriter<Title> ratingWriter() {
    return new JpaItemWriterBuilder<Title>().entityManagerFactory(emf).build();
  }

}
