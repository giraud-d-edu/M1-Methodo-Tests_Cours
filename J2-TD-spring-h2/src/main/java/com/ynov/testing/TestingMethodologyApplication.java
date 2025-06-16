package com.ynov.testing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application Class
 * 
 * This is the entry point of our Spring Boot application.
 * The @SpringBootApplication annotation is a convenience annotation that adds:
 * - @Configuration: Tags the class as a source of bean definitions
 * - @EnableAutoConfiguration: Tells Spring Boot to start adding beans based on classpath settings
 * - @ComponentScan: Tells Spring to look for other components, configurations, and services
 * 
 * @author Testing Methodology Course
 * @version 1.0.0
 */
@SpringBootApplication
public class TestingMethodologyApplication {

    /**
     * Main method that serves as the entry point for the Spring Boot application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(TestingMethodologyApplication.class, args);
    }
}
