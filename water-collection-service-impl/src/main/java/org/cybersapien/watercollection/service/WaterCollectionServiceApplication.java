package org.cybersapien.watercollection.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.cybersapien.watercollection")
public class WaterCollectionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WaterCollectionServiceApplication.class, args);
	}
}
