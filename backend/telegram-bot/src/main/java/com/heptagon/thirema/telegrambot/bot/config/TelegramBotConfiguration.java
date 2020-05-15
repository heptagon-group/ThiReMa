package com.heptagon.thirema.telegrambot.bot.config;

import com.heptagon.thirema.telegrambot.bot.TelegramBot;
import com.heptagon.thirema.telegrambot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

@Configuration
@EnableConfigurationProperties(TelegramBotProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TelegramBotConfiguration {

    private final TelegramBotProperties properties;
    private final UserRepository userRepository;

    @Bean
    public TelegramBot telegramBot() {
        return new TelegramBot(properties.getUsername(), properties.getToken(), userRepository);
    }
}
