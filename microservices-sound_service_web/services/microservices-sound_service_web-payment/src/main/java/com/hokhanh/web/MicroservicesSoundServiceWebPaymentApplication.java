package com.hokhanh.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MicroservicesSoundServiceWebPaymentApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicesSoundServiceWebPaymentApplication.class, args);
	}

}
