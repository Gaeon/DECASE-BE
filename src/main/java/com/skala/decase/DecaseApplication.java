package com.skala.decase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class DecaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(DecaseApplication.class, args);
	}

}
