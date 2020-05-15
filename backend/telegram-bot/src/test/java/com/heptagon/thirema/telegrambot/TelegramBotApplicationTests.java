package com.heptagon.thirema.telegrambot;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootTest
class TelegramBotApplicationTests {

    @BeforeAll
    public static void init() {
        ApiContextInitializer.init();
    }

    @Test
    void contextLoads() {
    }
}
