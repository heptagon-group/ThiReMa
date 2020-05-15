package com.heptagon.thirema.telegrambot.bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.inject.Inject;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class TelegramBotRegister implements ApplicationListener<ApplicationReadyEvent> {

    private final TelegramBot bot;

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent applicationReadyEvent) {
        try {
            TelegramBotsApi api = new TelegramBotsApi();
            api.registerBot(bot);
        } catch (TelegramApiException e) {
            log.error("Could not register bot", e);
        }
        log.info("Successfully registered bot " + bot.getBotUsername());
    }
}
