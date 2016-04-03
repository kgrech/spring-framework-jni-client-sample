package com.github.kgrech.statcollectior.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@Configuration
@ImportResource("classpath:/clients.xml")
public class StatCollectorServerApplication {

    /**
     * Application entry point. Starts spring boot application
     * @param args cli arguments list
     */
    public static void main(String[] args) {
        SpringApplication.run(StatCollectorServerApplication.class, args);
    }
}
