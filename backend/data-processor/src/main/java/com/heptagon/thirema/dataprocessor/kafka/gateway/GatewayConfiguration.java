package com.heptagon.thirema.dataprocessor.kafka.gateway;

import com.heptagon.thirema.dataprocessor.kafka.event.EventPublisher;
import com.heptagon.thirema.dataprocessor.repository.DeviceRepository;
import com.heptagon.thirema.dataprocessor.repository.MeasureDataRepository;
import com.heptagon.thirema.dataprocessor.repository.MeasureRepository;
import com.heptagon.thirema.dataprocessor.repository.UserRepository;
import io.confluent.kafka.serializers.AbstractKafkaAvroSerDeConfig;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.MessageListenerContainer;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(KafkaProperties.class)
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GatewayConfiguration {

    private static final String NUM_PARTITIONS_CONFIG = "num-partitions";
    private static final String REPLICATION_FACTOR_CONFIG = "replication-factor";

    private final KafkaProperties properties;
    private final MeasureDataRepository measureDataRepository;
    private final MeasureRepository measureRepository;
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;

    private Map<String, Object> commonConfig() {
        Map<String, Object> commonConfig = new HashMap<>();
        commonConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        commonConfig.put(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG,
                properties.getProperties().get(AbstractKafkaAvroSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG));
        return commonConfig;
    }

    private Map<String, Object> producerConfig() {
        Map<String, Object> producerConfig = commonConfig();
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

    private KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public GatewaySender gatewaySender() {
        return new GatewaySender(kafkaTemplate(), gatewayTopicHandler());
    }

    private Map<String, Object> consumerConfig() {
        Map<String, Object> consumerConfig = commonConfig();
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        consumerConfig.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);
        return consumerConfig;
    }

    private ConsumerFactory<String, Object> consumerFactory() {
        KafkaAvroDeserializer deserializer = new KafkaAvroDeserializer();
        deserializer.configure(consumerConfig(), false);
        return new DefaultKafkaConsumerFactory<>(consumerConfig(),
                new StringDeserializer(),  deserializer);
    }

    private KafkaListenerContainerFactory<? extends MessageListenerContainer> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> containerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setContainerCustomizer(container ->
                container.setupMessageListener(new GatewayReceiverMessageListener(measureDataRepository,
                        measureRepository, deviceRepository, userRepository, eventPublisher))
        );
        containerFactory.setConsumerFactory(consumerFactory());
        containerFactory.setAutoStartup(false);
        return containerFactory;
    }

    @Bean
    public GatewayReceiver gatewayReceiver() {
        return new GatewayReceiver(kafkaListenerContainerFactory(), properties.getClientId());
    }

    private AdminClient adminClient() {
        final Map<String, Object> adminConfig = commonConfig();
        adminConfig.put(AdminClientConfig.CLIENT_ID_CONFIG, properties.getClientId());
        adminConfig.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG,
                Integer.valueOf(properties.getAdmin().getProperties().get(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG)));
        return AdminClient.create(adminConfig);
    }

    @Bean
    public GatewayTopicHandler gatewayTopicHandler() {
        return new GatewayTopicHandler(
                adminClient(),
                Integer.parseInt(properties.getProperties().get(NUM_PARTITIONS_CONFIG)),
                Short.parseShort(properties.getProperties().get(REPLICATION_FACTOR_CONFIG))
        );
    }
}
