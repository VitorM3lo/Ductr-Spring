package com.ductr.ductrimdb.config;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import com.ductr.ductrimdb.entity.Genre;
import com.ductr.ductrimdb.entity.Title;
import com.ductr.ductrimdb.entity.Type;
import com.ductr.ductrimdb.listener.GenericJobListener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.FileSystemResource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.validation.BindException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableBatchProcessing
public class TitleIndexerJobConfiguration extends DefaultBatchConfigurer {

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
  public Job indexTitles() {
    return jobBuilderFactory.get("titleIndexer").listener(getListener()).start(interpretFile()).build();
  }

  @Bean
  public Step interpretFile() {
    return stepBuilderFactory.get("interpretFile").transactionManager(jpaTransactionManager()).<Title, Title>chunk(1000)
        .reader(titleReader()).writer(titleWriter()).build();
  }

  @Bean
  public JobExecutionListener getListener() {
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
    tokenizer.setStrict(false);
    tokenizer.setQuoteCharacter('\0');;
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

  @Bean
  @Primary
  public JpaTransactionManager jpaTransactionManager() {
    final JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setDataSource(this.dataSource);
    return transactionManager;
  }

  private class TitleMapper implements FieldSetMapper<Title> {

    @Override
    public Title mapFieldSet(FieldSet fieldSet) throws BindException {
      if (fieldSet == null) {
        return null;
      }

      Title title = new Title();
      String helper = null;

      title.setTconst(fieldSet.readString("tconst"));
      title.setTitleType(new Type(fieldSet.readString("titleType")));
      title.setPrimaryTitle(fieldSet.readString("primaryTitle"));
      title.setOriginalTitle(fieldSet.readString("originalTitle"));
      helper = fieldSet.readString("isAdult");
      title.setAdult(!helper.equals("\\N") ? Integer.parseInt(helper) == 1 : false);
      helper = fieldSet.readString("startYear");
      title.setStartYear(!helper.equals("\\N") ? Integer.parseInt(helper) : 0);
      helper = fieldSet.readString("endYear");
      title.setEndYear(!helper.equals("\\N") ? Integer.parseInt(helper) : 0);
      helper = fieldSet.readString("runtimeMinutes");
      title.setRuntimeMinutes(!helper.equals("\\N") ? Integer.parseInt(helper) : 0);
      title.setGenres(Arrays.asList(fieldSet.readString("genres").split(",")).stream().map(g -> new Genre(g))
          .collect(Collectors.toList()));

      log.info("Read: " + title.getTconst());
      return title;
    }
  }

}
