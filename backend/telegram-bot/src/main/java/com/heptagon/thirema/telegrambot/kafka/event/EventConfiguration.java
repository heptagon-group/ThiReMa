package com.heptagon.thirema.telegrambot.kafka.event;

import com.heptagon.thirema.commons.event.ThresholdExceededEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableConfigurationProperties(KafkaProperties.class)
@RequiredArgsConstructor
public class EventConfiguration {

    private final KafkaProperties properties;

    private Map<String, Object> consumerConfig(String clientId) {
        Map<String, Object> consumerConfig = new HashMap<>();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        consumerConfig.put(ConsumerConfig.CLIENT_ID_CONFIG, properties.getClientId() + "-" + clientId);
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getClientId() + "-" + clientId);
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return consumerConfig;
    }

    private <E> DefaultKafkaConsumerFactory<String, E> consumerFactory(String clientId, Class<E> eventClass) {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(clientId),
                new StringDeserializer(), new JsonDeserializer<>(eventClass));
    }

    private <E> ConcurrentKafkaListenerContainerFactory<String, E> containerFactory(String clientId, Class<E> eventClass) {
        ConcurrentKafkaListenerContainerFactory<String, E> containerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory(clientId, eventClass));
        return containerFactory;
    }

    @Bean("thresholdEventContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, ThresholdExceededEvent> thresholdEventContainerFactory() {
        return containerFactory("threshold-event-listener", ThresholdExceededEvent.class);
    }
}