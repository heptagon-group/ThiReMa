package com.heptagon.thirema.dataprocessor.kafka.gateway;

import com.heptagon.thirema.commons.domain.Gateway;
import com.heptagon.thirema.dataprocessor.repository.GatewayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GatewayTopicInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final GatewayRepository repository;
    private final GatewayTopicHandler topicHandler;
    private final GatewayReceiver receiver;

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        for (Gateway gateway : repository.findAll()) {
            topicHandler.createTopics(gateway.getId());
            receiver.startConsumer(topicHandler.getConsumerTopic(gateway.getId()));
        }
    }
}
