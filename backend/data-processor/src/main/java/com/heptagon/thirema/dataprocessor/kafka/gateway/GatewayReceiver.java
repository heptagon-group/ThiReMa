package com.heptagon.thirema.dataprocessor.kafka.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.listener.MessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class GatewayReceiver {

    private final Map<String, MessageListenerContainer> containers = new HashMap<>();
    private final KafkaListenerContainerFactory<? extends MessageListenerContainer> containerFactory;
    private final String clientId;

    public void startConsumer(String topic) {
        MessageListenerContainer container = containerFactory.createContainer(topic);
        String id = clientId + topic;
        container.getContainerProperties().setGroupId(id);
        container.getContainerProperties().setClientId(id);
        container.start();
        containers.put(topic, container);
    }

    public void stopConsumer(String topic) {
        MessageListenerContainer container = containers.get(topic);
        container.stop();
        containers.remove(topic);
    }
}