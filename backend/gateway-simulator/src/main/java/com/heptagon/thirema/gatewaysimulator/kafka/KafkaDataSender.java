package com.heptagon.thirema.gatewaysimulator.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@RequiredArgsConstructor
public class KafkaDataSender {

    private final KafkaTemplate<String, Object> template;

    public void send(String topic, Object dataObject) {
        log.info("Sending data to " + topic + " : " + dataObject);
        template.send(topic, dataObject);
    }
}
