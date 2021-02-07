package com.ductr.ductrimdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EntityScan("com.ductr.ductrentity.entities")
@EnableCaching
public class DuctrImdbApplication {

	public static void main(String[] args) {
		SpringApplication.run(DuctrImdbApplication.class, args);
	}

}
