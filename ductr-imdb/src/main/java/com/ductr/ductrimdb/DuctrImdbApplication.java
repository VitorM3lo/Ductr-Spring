package com.ductr.ductrimdb;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DuctrImdbApplication {

	public static void main(String[] args) {
		ApplicationContext appContext = SpringApplication.run(DuctrImdbApplication.class, args);
		JobLauncher jobLauncher = (JobLauncher) appContext.getBean("jobLauncher");
		Job titleJob = (Job) appContext.getBean("titleIndexerJob");
		Job ratingsJob = (Job) appContext.getBean("ratingsIndexerJob");
		Job titleRegionJob = (Job) appContext.getBean("titleRegionIndexerJob");
		try {
			// jobLauncher.run(titleJob, new JobParameters());
			// jobLauncher.run(ratingsJob, new JobParameters());
			jobLauncher.run(titleRegionJob, new JobParameters());
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
		}
	}

}
