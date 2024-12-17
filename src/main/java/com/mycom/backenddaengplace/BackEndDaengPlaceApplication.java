package com.mycom.backenddaengplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BackEndDaengPlaceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackEndDaengPlaceApplication.class, args);
    }
}
