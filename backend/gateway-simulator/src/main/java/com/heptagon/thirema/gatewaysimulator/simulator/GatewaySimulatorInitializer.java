package com.heptagon.thirema.gatewaysimulator.simulator;

import com.heptagon.thirema.gatewaysimulator.kafka.KafkaDataSender;
import com.heptagon.thirema.gatewaysimulator.kafka.TopicGetter;
import com.heptagon.thirema.gatewaysimulator.util.RandomSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Map;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GatewaySimulatorInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final KafkaDataSender kafkaSender;
    private final TopicGetter topic;
    private final Map<String, Device> devices;

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        // inizializzazione device predefiniti
        Device device2 = new Device("123.12.1.3", 60,
                Arrays.asList("Temperatura", "Umidità"),
                new RandomSupplier(15.0D, 25.0D), new RandomSupplier(40.0D, 55.0D));
        devices.put(device2.getIpAddress(), device2);

        GatewaySimulatorSender sender = new GatewaySimulatorSender(devices, kafkaSender, topic);
        Thread senderThread = new Thread(sender);
        senderThread.start();
    }
}
