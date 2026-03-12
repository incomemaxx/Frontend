package org.example.matching.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.example.matching")
public class MatchingEngineApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatchingEngineApplication.class, args);
    }
}
