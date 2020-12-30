package com.ductr.ductrimdb.listener;

import com.ductr.ductrimdb.repository.IMDbFileRepository;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenericJobListener implements JobExecutionListener, InitializingBean {

  @Autowired
  IMDbFileRepository fileRepository;

  private String file;

  public void setFile(String file) {
    this.file = file;
  }

  @Override
  public void beforeJob(JobExecution jobExecution) {
    this.fileRepository.getFile(file);
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    log.info("Title basic data indexing complete...");
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Assert.notNull(file, "file must be set");
  }
  
}
