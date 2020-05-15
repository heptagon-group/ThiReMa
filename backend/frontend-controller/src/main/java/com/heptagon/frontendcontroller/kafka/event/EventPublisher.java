package com.heptagon.frontendcontroller.kafka.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class EventPublisher {

    private final KafkaTemplate<String, Object> template;

    public void publishEvent(String topic, Object event) {
        template.send(topic, event);
    }
}
