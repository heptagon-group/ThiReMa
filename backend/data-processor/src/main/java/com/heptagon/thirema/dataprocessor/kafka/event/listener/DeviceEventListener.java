package com.heptagon.thirema.dataprocessor.kafka.event.listener;

import com.heptagon.thirema.commons.domain.Device;
import com.heptagon.thirema.commons.event.EntityEvent;
import com.heptagon.thirema.dataprocessor.kafka.gateway.GatewaySender;
import com.heptagon.thirema.dataprocessor.kafka.gateway.avro.AvroDeviceConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DeviceEventListener {

    private final GatewaySender sender;

    @KafkaListener(topics = EntityEvent.DEVICE_TOPIC, containerFactory = "deviceEventContainerFactory")
    public void onDeviceEvent(EntityEvent<Device> event) {
        Device device = event.getEntity();
        AvroDeviceConfig deviceConfig;
        switch (event.getState()) {
            case CREATED:
            case UPDATED:
                deviceConfig = AvroDeviceConfig.newBuilder()
                        .setIpAddress(device.getIpAddress())
                        .setFrequency(device.getFrequency())
                        .setEnabled(true)
                        .build();
                break;
            case DELETED:
                deviceConfig = AvroDeviceConfig.newBuilder()
                        .setEnabled(false)
                        .build();
                break;
            default:
                throw new IllegalArgumentException("Unknown EntityState " + event.getState());
        }
        sender.sendData(device.getGatewayId(), deviceConfig);
    }
}
