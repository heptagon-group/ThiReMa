package com.heptagon.thirema.dataprocessor.kafka.event.listener;

import com.heptagon.thirema.commons.domain.Measure;
import com.heptagon.thirema.commons.event.EntityEvent;
import com.heptagon.thirema.dataprocessor.kafka.gateway.GatewaySender;
import com.heptagon.thirema.dataprocessor.kafka.gateway.avro.AvroMeasureConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MeasureEventListener {

    private final GatewaySender sender;

    @KafkaListener(topics = EntityEvent.MEASURE_TOPIC, containerFactory = "measureEventContainerFactory")
    public void onMeasureEvent(EntityEvent<Measure> event) {
        Measure measure = event.getEntity();
        AvroMeasureConfig measureConfig;
        switch (event.getState()) {
            case CREATED:
            case UPDATED:
                measureConfig = AvroMeasureConfig.newBuilder()
                        .setIpAddress(measure.getDevice().getIpAddress())
                        .setName(measure.getName())
                        .setEnabled(true)
                        .build();
                break;
            case DELETED:
                measureConfig = AvroMeasureConfig.newBuilder()
                        .setEnabled(false)
                        .build();
                break;
            default:
                throw new IllegalArgumentException("Unknown EntityState " + event.getState());
        }
        sender.sendData(measure.getDevice().getGatewayId(), measureConfig);
    }
}
