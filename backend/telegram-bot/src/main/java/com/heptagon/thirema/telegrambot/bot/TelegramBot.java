package com.heptagon.thirema.telegrambot.bot;

import com.heptagon.thirema.telegrambot.bot.command.StartCommand;
import com.heptagon.thirema.telegrambot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class TelegramBot extends TelegramLongPollingCommandBot {

    private final String username;
    private final String token;

    public TelegramBot(String username, String token, UserRepository userRepository) {
        super(ApiContext.getInstance(DefaultBotOptions.class),false);
        this.username = username;
        this.token = token;

        register(new StartCommand(userRepository));

        registerDefaultAction((sender, message) ->
                sendUnknownCommandMessage(message.getChatId()));
    }

    private void sendUnknownCommandMessage(Long chatId) {
        SendMessage commandUnknown = new SendMessage(chatId, "Comando sconosciuto");
        try {
            execute(commandUnknown);
        } catch (TelegramApiException e) {
            log.error("Could not send command unknown message", e);
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage()) {
            sendUnknownCommandMessage(update.getMessage().getChatId());
        }
    }
}
