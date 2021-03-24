package com.gmart.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableEurekaClient
@EnableTransactionManagement
@SpringBootApplication
@EnableAutoConfiguration
public class GmartWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmartWsApplication.class, args);
	}

}
