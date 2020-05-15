package com.heptagon.thirema.dataprocessor.kafka.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class GatewaySender {

    private final KafkaTemplate<String, Object> template;
    private final GatewayTopicHandler topicHandler;

    public void sendData(long gatewayId, Object data) {
        template.send(topicHandler.getProducerTopic(gatewayId), data);
    }
}