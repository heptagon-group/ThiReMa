package com.heptagon.thirema.gatewaysimulator.simulator;

import com.heptagon.thirema.dataprocessor.kafka.gateway.avro.AvroDeviceConfig;
import com.heptagon.thirema.dataprocessor.kafka.gateway.avro.AvroMeasureConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Map;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GatewaySimulatorReceiver {

    private final Map<String, Device> devices;

    @KafkaListener(topics = "#{@topicGetter.getConsumerTopic()}")
    public void onConfiguration(Object config) {
        if (config instanceof AvroDeviceConfig) {
            AvroDeviceConfig deviceConfig = (AvroDeviceConfig) config;

            Device device = devices.get(deviceConfig.getIpAddress().toString());
            if (device == null) {
                return;
            }
            device.setEnabled(deviceConfig.getEnabled());

            if (device.isEnabled()) {
                device.setFrequency(deviceConfig.getFrequency());
            }
        } else if (config instanceof AvroMeasureConfig) {
            AvroMeasureConfig measureConfig = (AvroMeasureConfig) config;
            Device device = devices.get(measureConfig.getIpAddress().toString());
            if (device == null) {
                return;
            }

            if (measureConfig.getEnabled()) {
                device.getMeasures().add(measureConfig.getName().toString());
            } else {
                device.getMeasures().remove(measureConfig.getName().toString());
            }
        }
    }
}
