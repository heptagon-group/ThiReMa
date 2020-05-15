package com.heptagon.thirema.telegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EntityScan("com.heptagon.thirema.commons.domain")
public class TelegramBotApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(TelegramBotApplication.class, args);
    }
}
