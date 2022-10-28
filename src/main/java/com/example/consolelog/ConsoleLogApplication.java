package com.example.consolelog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ConsoleLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsoleLogApplication.class, args);
    }

}
