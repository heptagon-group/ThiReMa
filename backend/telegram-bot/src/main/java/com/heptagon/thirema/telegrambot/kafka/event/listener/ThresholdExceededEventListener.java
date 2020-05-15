package com.heptagon.thirema.telegrambot.kafka.event.listener;

import com.heptagon.thirema.commons.event.ThresholdExceededEvent;
import com.heptagon.thirema.telegrambot.bot.TelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.inject.Inject;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ThresholdExceededEventListener {

    private final TelegramBot bot;

    @KafkaListener(topics = ThresholdExceededEvent.TOPIC, containerFactory = "thresholdEventContainerFactory")
    public void onThresholdExceededEvent(ThresholdExceededEvent event) {
        // utente non ha ancora chattato per la prima volta con il bot
        if (event.getUser().getTelegramUserId() == null) {
            return;
        }
        String deviceName = event.getDevice().getName();
        String measureName = event.getMeasure().getName();
        String threshold = event.getMeasure().getThreshold() + event.getMeasure().getFormat();
        String value = event.getMeasureValue() + event.getMeasure().getFormat();
        String message = String.format("<b>[ATTENZIONE]</b>\n" +
                "Nel device %1$s è stata superata la soglia di %2$s impostata a %3$s, arrivando a %4$s!",
                deviceName, measureName, threshold, value);

        SendMessage alert = new SendMessage(event.getUser().getTelegramUserId().longValue(), message)
                .enableHtml(true);
        try {
            bot.execute(alert);
        } catch (TelegramApiException e) {
            log.error("Could not send alert message", e);
        }
    }
}
