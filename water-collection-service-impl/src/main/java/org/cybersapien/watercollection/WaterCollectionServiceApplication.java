package org.cybersapien.watercollection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Main application for water collection service
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
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
