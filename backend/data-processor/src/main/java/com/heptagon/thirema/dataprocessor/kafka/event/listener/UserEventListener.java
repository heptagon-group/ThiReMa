package com.heptagon.thirema.dataprocessor.kafka.event.listener;

import com.heptagon.thirema.commons.domain.Gateway;
import com.heptagon.thirema.commons.domain.User;
import com.heptagon.thirema.commons.event.EntityEvent;
import com.heptagon.thirema.commons.event.EntityState;
import com.heptagon.thirema.dataprocessor.repository.GatewayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserEventListener {

    private final GatewayRepository gatewayRepository;
    private final GatewayEventListener gatewayEventListener;

    @KafkaListener(topics = EntityEvent.USER_TOPIC, containerFactory = "userEventContainerFactory")
    public void onUserEvent(EntityEvent<User> event) {
        if (event.getState() == EntityState.DELETED) {
            for (Gateway gateway : gatewayRepository.findAllByOwnerId(event.getEntity().getId())) {
                gatewayEventListener.onGatewayDeleted(gateway);
            }
        }
    }
}
