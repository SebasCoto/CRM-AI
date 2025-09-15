package com.crmvital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CrmvitalApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrmvitalApplication.class, args);
    }
}
