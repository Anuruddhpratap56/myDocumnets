package com.mydocs.mydocs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MydocsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MydocsApplication.class, args);
	}

}
