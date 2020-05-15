package com.heptagon.frontendcontroller.service;

import com.heptagon.frontendcontroller.controller.exception.GatewayNotFoundException;
import com.heptagon.frontendcontroller.kafka.event.EventPublisher;
import com.heptagon.frontendcontroller.repository.GatewayRepository;
import com.heptagon.thirema.commons.domain.Gateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.heptagon.frontendcontroller.controller.util.GatewayUtil.createGateway;
import static com.heptagon.frontendcontroller.controller.util.MessageUtil.GATEWAY_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GatewayService.class})
class GatewayServiceTest {

    @Inject
    private GatewayService gatewayService;

    @MockBean
    private GatewayRepository gatewayRepository;

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    public void getGatewayList_returnGatewayList() {
        List<Gateway> gatewayList = Arrays.asList(createGateway(), createGateway());
        when(gatewayRepository.findAllByOwnerId(1L)).thenReturn(gatewayList);

        List<Gateway> gatewayListTest = gatewayService.getGatewayList(1L);

        assertEquals(gatewayListTest, gatewayList);
    }

    @Test
    public void getNewGatewayId_okAndReturnId() {
        when(gatewayRepository.save(any(Gateway.class))).thenReturn(createGateway());

        Gateway newGateway = gatewayService.getNewGatewayId(Gateway.builder().ownerId(1L).build());

        verify(gatewayRepository, times(1)).save(any(Gateway.class));
        assertEquals(1L, newGateway.getId());
    }

    @Test
    public void deleteGateway_gatewayDoesNotExist_throwGatewayNotFoundException() {
        assertThrows(GatewayNotFoundException.class,
                () -> gatewayService.deleteGateway(1L, 1L), GATEWAY_NOT_FOUND);
    }

    @Test
    public void deleteGateway_gatewayExists_ok() {
        when(gatewayRepository.findByIdAndOwnerId(1L, 1L)).thenReturn(Optional.of(createGateway()));

        gatewayService.deleteGateway(1L, 1L);

        verify(gatewayRepository, times(1)).deleteById(1L);
    }
}