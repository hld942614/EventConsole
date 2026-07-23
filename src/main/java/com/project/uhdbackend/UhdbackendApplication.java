package com.project.uhdbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;

@SpringBootApplication
@EnableIntegration
public class UhdbackendApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(UhdbackendApplication.class, args);
	}
}
