package com.hokhanh.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class MicroservicesSoundServiceWebDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicesSoundServiceWebDiscoveryApplication.class, args);
	}

}
