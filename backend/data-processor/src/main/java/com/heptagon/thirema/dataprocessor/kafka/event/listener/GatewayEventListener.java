package com.heptagon.thirema.dataprocessor.kafka.event.listener;

import com.heptagon.thirema.commons.domain.Gateway;
import com.heptagon.thirema.commons.event.EntityEvent;
import com.heptagon.thirema.commons.event.EntityState;
import com.heptagon.thirema.dataprocessor.kafka.gateway.GatewayReceiver;
import com.heptagon.thirema.dataprocessor.kafka.gateway.GatewayTopicHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GatewayEventListener {

    private final GatewayTopicHandler topicHandler;
    private final GatewayReceiver receiver;

    @KafkaListener(topics = EntityEvent.GATEWAY_TOPIC, containerFactory = "gatewayEventContainerFactory")
    public void onGatewayEvent(EntityEvent<Gateway> event) {
        if (event.getState() == EntityState.CREATED) {
            onGatewayCreated(event.getEntity());
        } else if (event.getState() == EntityState.DELETED) {
            onGatewayDeleted(event.getEntity());
        }
    }

    // crea i topic e il consumer
    public void onGatewayCreated(Gateway gateway) {
        topicHandler.createTopics(gateway.getId());
        receiver.startConsumer(topicHandler.getConsumerTopic(gateway.getId()));
    }

    // elimina il consumer e i topic
    public void onGatewayDeleted(Gateway gateway) {
        receiver.stopConsumer(topicHandler.getConsumerTopic(gateway.getId()));
        topicHandler.deleteTopics(gateway.getId());
    }
}