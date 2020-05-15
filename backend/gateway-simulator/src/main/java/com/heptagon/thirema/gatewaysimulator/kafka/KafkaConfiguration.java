package com.heptagon.thirema.gatewaysimulator.kafka;

import com.heptagon.thirema.dataprocessor.kafka.gateway.avro.AvroDeviceConfig;
import com.heptagon.thirema.gatewaysimulator.simulator.Device;
import io.confluent.kafka.serializers.*;
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
import org.springframework.kafka.core.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableKafka
@EnableConfigurationProperties({KafkaProperties.class, GatewayProperties.class})
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class KafkaConfiguration {

    private final KafkaProperties kafkaProperties;
    private final GatewayProperties gatewayProperties;

    private Map<String, Object> commonConfig() {
        Map<String, Object> commonConfig = new HashMap<>();
        commonConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        commonConfig.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
                kafkaProperties.getProperties().get(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG));
        return commonConfig;
    }

    private Map<String, Object> producerConfig() {
        Map<String, Object> producerConfig = commonConfig();
        producerConfig.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProperties.getProducer().getClientId());
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        return producerConfig;
    }

    private ProducerFactory<String, Object> producerFactory() {
        KafkaAvroSerializer serializer = new KafkaAvroSerializer();
        serializer.configure(producerConfig(), false);
        return new DefaultKafkaProducerFactory<>(producerConfig(),
                new StringSerializer(),  serializer);
    }

    private KafkaTemplate<String, Object> kafkaProducerTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    private Map<String, Object> consumerConfig() {
        Map<String, Object> consumerConfig = commonConfig();
        consumerConfig.put(ConsumerConfig.CLIENT_ID_CONFIG, kafkaProperties.getConsumer().getClientId());
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        consumerConfig.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        return consumerConfig;
    }

    private ConsumerFactory<String, Object> consumerFactory() {
        KafkaAvroDeserializer deserializer = new KafkaAvroDeserializer();
        deserializer.configure(consumerConfig(), false);
        return new DefaultKafkaConsumerFactory<>(consumerConfig(),
                new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AvroDeviceConfig> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, AvroDeviceConfig> containerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConsumerFactory(consumerFactory());
        return containerFactory;
    }

    @Bean
    public KafkaDataSender dataPublisher() {
        return new KafkaDataSender(kafkaProducerTemplate());
    }

    @Bean("topicGetter")
    public TopicGetter topicGetter(){
        return new TopicGetter(gatewayProperties.getId());
    }

    @Bean
    public Map<String, Device> devicesMap() {
        return new ConcurrentHashMap<>();
    }
}
