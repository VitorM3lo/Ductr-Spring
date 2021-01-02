package com.ductr.ductrimdb.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import com.ductr.ductrimdb.listener.GenericJobListener;

import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;

@Configuration
@EnableBatchProcessing
public abstract class IndexerBatchConfigurer {

  @Autowired
  protected DataSource dataSource;

  @Autowired
  protected EntityManagerFactory emf;

  @Autowired
  protected JobBuilderFactory jobBuilderFactory;

  @Autowired
  protected StepBuilderFactory stepBuilderFactory;

  @Bean
  @Primary
  public JpaTransactionManager getTransactionManager() {
    final JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setDataSource(this.dataSource);
    return transactionManager;
  }

  @Bean
  public JobExecutionListener getJobListener(String file) {
    GenericJobListener jobListener = new GenericJobListener();
    jobListener.setFile(file);
    return jobListener;
  }

}
