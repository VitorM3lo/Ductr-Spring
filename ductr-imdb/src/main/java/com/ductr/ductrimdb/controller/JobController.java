package com.ductr.ductrimdb.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@EnableScheduling
public class JobController {

  @Autowired
  ApplicationContext applicationContext;

  @Autowired
  JobLauncher jobLauncher;

  private Thread runnerThread;

  @Scheduled(cron = "0 0 * * * 0")
  @RequestMapping("startJobs")
  public void executeJobs() {
    if (runnerThread == null) {
      runnerThread = new Thread(new JobRunner());
      runnerThread.start();
    }
  }

  private class JobRunner implements Runnable {
    public void run() {
      Job titleJob = (Job) applicationContext.getBean("titleIndexerJob");
      Job ratingsJob = (Job) applicationContext.getBean("ratingsIndexerJob");
      Job titleRegionJob = (Job) applicationContext.getBean("titleRegionIndexerJob");
      Job episodeJob = (Job) applicationContext.getBean("episodeIndexerJob");
      try {
        jobLauncher.run(titleJob, new JobParameters());
        jobLauncher.run(ratingsJob, new JobParameters());
        jobLauncher.run(titleRegionJob, new JobParameters());
        jobLauncher.run(episodeJob, new JobParameters());
        log.info("Jobs ended...");
      } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
          | JobParametersInvalidException e) {
        e.printStackTrace();
      }
    }
  }

}
