package com.example.SlaveNode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SlaveNodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlaveNodeApplication.class, args);
	}

}
