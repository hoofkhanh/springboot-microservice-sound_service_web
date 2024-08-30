package com.hokhanh.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicroservicesSoundServiceWebBeatApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicesSoundServiceWebBeatApplication.class, args);
	}

}
