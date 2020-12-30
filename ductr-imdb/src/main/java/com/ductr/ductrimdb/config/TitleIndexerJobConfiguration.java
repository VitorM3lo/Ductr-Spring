package com.ductr.ductrimdb.config;

import java.io.File;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import com.ductr.ductrimdb.entity.Title;
import com.ductr.ductrimdb.listener.GenericJobListener;
import com.ductr.ductrimdb.mapper.TitleMapper;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.orm.jpa.JpaTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableBatchProcessing
public class TitleIndexer extends DefaultBatchConfigurer {

  @Autowired
  DataSource dataSource;
  
  @Autowired
  EntityManagerFactory emf;
  
  @Autowired
  private JobBuilderFactory jobBuilderFactory;
  
  @Autowired
  private StepBuilderFactory stepBuilderFactory;
  
  @Value("${file.repository}")
  private String folder;
  
  @Value("${movie.basics.dataset}")
  private String file;
  
  @Bean
  @Primary
  public JpaTransactionManager jpaTransactionManager() {
    final JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setDataSource(this.dataSource);
    return transactionManager;
  }
  
  @Bean
  public Job indexerJob() {
    return jobBuilderFactory.get("indexerJob").listener(getTitleListener()).start(indexTitles()).build();
  }

  @Bean
  public Step indexTitles() {
    return stepBuilderFactory.get("indexTitles").transactionManager(jpaTransactionManager()).<Title, Title>chunk(1000)
        .reader(titleReader()).writer(titleWriter()).build();
  }

  @Bean
  public JobExecutionListener getTitleListener() {
    GenericJobListener jobListener = new GenericJobListener();
    jobListener.setFile(file);
    return jobListener;
  }

  @Bean
  @StepScope
  public FlatFileItemReader<Title> titleReader() {
    log.info("titleReader");
    FlatFileItemReader<Title> itemReader = new FlatFileItemReader<>();
    FileSystemResource resource = new FileSystemResource(new File(folder + file.replace(".gz", "")));
    DefaultLineMapper<Title> lineMapper = new DefaultLineMapper<>();
    DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_TAB);
    tokenizer.setNames(new String[] { "tconst", "titleType", "primaryTitle", "originalTitle", "isAdult", "startYear",
        "endYear", "runtimeMinutes", "genres" });
    tokenizer.setQuoteCharacter('\0');
    lineMapper.setLineTokenizer(tokenizer);
    lineMapper.setFieldSetMapper(new TitleMapper());
    itemReader.setResource(resource);
    itemReader.setLineMapper(lineMapper);
    itemReader.setLinesToSkip(1);
    itemReader.setStrict(false);
    itemReader.open(new ExecutionContext());
    return itemReader;
  }

  @Bean
  public JpaItemWriter<Title> titleWriter() {
    log.info("titleWriter");
    return new JpaItemWriterBuilder<Title>().entityManagerFactory(emf).build();
  }

}
