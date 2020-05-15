package com.heptagon.frontendcontroller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
@EntityScan("com.heptagon.thirema.commons.domain")
public class FrontendControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontendControllerApplication.class, args);
    }

}
