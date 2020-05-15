package com.heptagon.thirema.telegrambot.bot.command;

import com.heptagon.thirema.commons.domain.User;
import com.heptagon.thirema.telegrambot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.inject.Inject;
import java.util.Optional;

@Slf4j
public class StartCommand extends BotCommand {

    private UserRepository userRepository;

    @Inject
    public StartCommand(UserRepository userRepository) {
        super("start", "Inizia la conversazione con il bot");
        this.userRepository = userRepository;
    }

    @Override
    public void execute(AbsSender sender, org.telegram.telegrambots.meta.api.objects.User u,
                        Chat chat, String[] strings) {
        Optional<User> opt = userRepository.findByTelegramUsername(u.getUserName());
        if (opt.isPresent()) {
            User user = opt.get();
            // aggiorna telegramUserId se è la prima volta che usa il bot o se ha cambiato telegramUsername
            if (!u.getId().equals(user.getTelegramUserId())) {
                user.setTelegramUserId(u.getId());
                userRepository.save(user);
                SendMessage welcome = new SendMessage(chat.getId(), "Benvenuto nel bot di Heptagon ThiReMa!");
                try {
                    sender.execute(welcome);
                } catch (TelegramApiException e) {
                    log.error("Could not send welcome message", e);
                }
            }
        } else {
            SendMessage unregistered = new SendMessage(chat.getId(),
                    "Mi dispiace, ma non risulti registrato in Heptagon ThiReMa.");
            try {
                sender.execute(unregistered);
            } catch (TelegramApiException e) {
                log.error("Could not send unregistered message", e);
            }
        }
    }
}
