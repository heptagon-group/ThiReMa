package com.heptagon.thirema.telegrambot.bot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("telegram.bot")
@ConstructorBinding
@Data
public class TelegramBotProperties {

    private final String username;
    private final String token;
}
