package com.heptagon.thirema.dataprocessor.kafka.event;

import com.fasterxml.jackson.core.type.TypeReference;
import com.heptagon.thirema.commons.domain.Device;
import com.heptagon.thirema.commons.domain.Gateway;
import com.heptagon.thirema.commons.domain.Measure;
import com.heptagon.thirema.commons.domain.User;
import com.heptagon.thirema.commons.event.EntityEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@EnableConfigurationProperties(KafkaProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class EventConfiguration {

    private final KafkaProperties properties;

    private Map<String, Object> commonConfig(String clientId) {
        Map<String, Object> commonConfig = new HashMap<>();
        commonConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        commonConfig.put(ConsumerConfig.CLIENT_ID_CONFIG, properties.getClientId() + "-" + clientId);
        return commonConfig;
    }

    private Map<String, Object> consumerConfig(String clientId) {
        Map<String, Object> consumerConfig = commonConfig(clientId);
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getClientId() + "-" + clientId);
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return consumerConfig;
    }

    private <E> DefaultKafkaConsumerFactory<String, E> consumerFactory(String clientId, Class<E> eventClass) {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(clientId),
                new StringDeserializer(), new JsonDeserializer<>(eventClass));
    }

    private <E> DefaultKafkaConsumerFactory<String, E> consumerFactory(String clientId, TypeReference<E> eventClass) {
        return new DefaultKafkaConsumerFactory<>(consumerConfig(clientId),
                new StringDeserializer(), new JsonDeserializer<>(eventClass));
    }

    private <E> ConcurrentKafkaListenerContainerFactory<String, E> containerFactory(String clientId, Class<E> eventClass) {
        ConcurrentKafkaListenerContainerFactory<String, E> containerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory(clientId, eventClass));
        return containerFactory;
    }

    private <E> ConcurrentKafkaListenerContainerFactory<String, E> containerFactory(String clientId, TypeReference<E> eventClass) {
        ConcurrentKafkaListenerContainerFactory<String, E> containerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory(clientId, eventClass));
        return containerFactory;
    }

    @Bean("userEventContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, EntityEvent<User>> userEventContainerFactory() {
        return containerFactory("user-event-listener", new TypeReference<EntityEvent<User>>() {});
    }

    @Bean("gatewayEventContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, EntityEvent<Gateway>> gatewayEventContainerFactory() {
        return containerFactory("gateway-event-listener", new TypeReference<EntityEvent<Gateway>>() {});
    }

    @Bean("deviceEventContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, EntityEvent<Device>> deviceEventContainerFactory() {
        return containerFactory("device-event-listener", new TypeReference<EntityEvent<Device>>() {});
    }

    @Bean("measureEventContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, EntityEvent<Measure>> measureEventContainerFactory() {
        return containerFactory("measure-event-listener", new TypeReference<EntityEvent<Measure>>() {});
    }

    private Map<String, Object> producerConfig() {
        Map<String, Object> producerConfig = commonConfig("event-publisher");
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
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