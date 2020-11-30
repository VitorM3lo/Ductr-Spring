package com.ductr.ductrimdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DuctrImdbApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuctrImdbApplication.class, args);
	}

}
