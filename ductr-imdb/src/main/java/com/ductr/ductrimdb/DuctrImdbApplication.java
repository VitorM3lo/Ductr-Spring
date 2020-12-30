package com.ductr.ductrimdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class DuctrImdbApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuctrImdbApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void beginIndex() {

	}

}
