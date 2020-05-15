package com.heptagon.frontendcontroller.kafka.event;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EventConfiguration {

    private final KafkaProperties properties;

    private Map<String, Object> producerConfig() {
        Map<String, Object> producerConfig = new HashMap<>();
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        producerConfig.put(ProducerConfig.CLIENT_ID_CONFIG, properties.getClientId());
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return producerConfig;
    }

    private ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig(),
                new StringSerializer(), new JsonSerializer<>().noTypeInfo());
    }

    private KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public EventPublisher eventPublisher() {
        return new EventPublisher(kafkaTemplate());
    }
}