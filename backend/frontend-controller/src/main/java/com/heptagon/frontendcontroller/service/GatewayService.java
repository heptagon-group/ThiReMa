package com.heptagon.frontendcontroller.service;

import com.heptagon.frontendcontroller.controller.exception.GatewayNotFoundException;
import com.heptagon.frontendcontroller.kafka.event.EventPublisher;
import com.heptagon.frontendcontroller.repository.GatewayRepository;
import com.heptagon.thirema.commons.domain.Gateway;
import com.heptagon.thirema.commons.event.EntityEvent;
import com.heptagon.thirema.commons.event.EntityState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GatewayService {

    private final GatewayRepository gatewayRepository;
    private final EventPublisher eventPublisher;

    public List<Gateway> getGatewayList(long ownerId) {
        return (List<Gateway>) gatewayRepository.findAllByOwnerId(ownerId);
    }

    public Gateway getNewGatewayId(Gateway gateway) {
        Gateway saved = gatewayRepository.save(gateway);
        publishGatewayEvent(saved, EntityState.CREATED);
        return saved;
    }

    public void deleteGateway(long id, long ownerId) {
        Optional<Gateway> opt = gatewayRepository.findByIdAndOwnerId(id, ownerId);
        if (opt.isPresent()) {
            gatewayRepository.deleteById(id);
            publishGatewayEvent(opt.get(), EntityState.DELETED);
        } else {
            throw new GatewayNotFoundException();
        }
    }



    private void publishGatewayEvent(Gateway gateway, EntityState state) {
        EntityEvent<Gateway> event = new EntityEvent<>(gateway, state);
        eventPublisher.publishEvent(EntityEvent.GATEWAY_TOPIC, event);
    }
}
