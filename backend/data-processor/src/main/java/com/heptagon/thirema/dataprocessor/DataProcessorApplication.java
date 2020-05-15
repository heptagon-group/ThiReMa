package com.heptagon.thirema.dataprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.heptagon.thirema.commons.domain")
public class DataProcessorApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataProcessorApplication.class, args);
    }

}
