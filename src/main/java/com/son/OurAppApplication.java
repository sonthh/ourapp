package com.son;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OurAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(OurAppApplication.class, args);
    }
}
