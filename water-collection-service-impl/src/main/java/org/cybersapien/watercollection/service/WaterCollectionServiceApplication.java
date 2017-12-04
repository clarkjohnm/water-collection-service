package org.cybersapien.watercollection.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main application for water collection service
 */
@SpringBootApplication
@ComponentScan("org.cybersapien.watercollection")
public class WaterCollectionServiceApplication {

    /**
     * Entrypoint for the water collection service
     *
     * @param args commandline arguments
     */
	public static void main(String[] args) {
		SpringApplication.run(WaterCollectionServiceApplication.class, args);
	}
}
