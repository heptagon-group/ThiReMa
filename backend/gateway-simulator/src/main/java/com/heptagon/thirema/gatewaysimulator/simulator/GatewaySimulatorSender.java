package com.heptagon.thirema.gatewaysimulator.simulator;

import com.heptagon.thirema.dataprocessor.kafka.gateway.avro.AvroDeviceData;
import com.heptagon.thirema.dataprocessor.kafka.gateway.avro.AvroMeasureData;
import com.heptagon.thirema.gatewaysimulator.kafka.KafkaDataSender;
import com.heptagon.thirema.gatewaysimulator.kafka.TopicGetter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GatewaySimulatorSender implements Runnable {

    private final Map<String, Device> devices;
    private final KafkaDataSender sender;
    private final TopicGetter topic;
    private long elapsed = 0;

    @SuppressWarnings("InfiniteLoopStatement")
    @SneakyThrows
    @Override
    public void run() {
        while (true){
            for (Device device : devices.values()) {
                if (device.isEnabled() && elapsed % device.getFrequency() == 0) {
                    sendDeviceData(device);
                }
            }
            elapsed++;
            Thread.sleep(1000L);
        }
    }

    private void sendDeviceData(Device device) {
        AvroDeviceData deviceData = AvroDeviceData.newBuilder()
                .setIpAddress(device.getIpAddress())
                .setMeasures(device.getMeasures().stream()
                        .map(name -> new AvroMeasureData(name, device.getData(name)))
                        .collect(Collectors.toList()))
                .build();
        sender.send(topic.getProducerTopic(), deviceData);
    }
}
