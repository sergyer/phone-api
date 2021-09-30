package com.example.phoneapi;

import com.example.phoneapi.infrastructure.WikipediaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(value = WikipediaProperties.class)
@SpringBootApplication
public class PhoneApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PhoneApiApplication.class, args);
    }
}
