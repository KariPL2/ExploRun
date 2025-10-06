package com.example.ExploRun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@Profile("ExploRun")
@SpringBootApplication
public class ExploRunApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExploRunApplication.class, args);
	}

}
