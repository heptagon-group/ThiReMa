package com.heptagon.thirema.dataprocessor.kafka.gateway;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GatewayTopicHandler {

    private final AdminClient adminClient;
    private final int numPartitions;
    private final short replicationFactor;

    public void createTopics(long gatewayId) {
        adminClient.createTopics(getTopics(gatewayId).stream()
                .map(topic -> new NewTopic(topic, numPartitions, replicationFactor))
                .collect(Collectors.toList()));
    }

    public void deleteTopics(long gatewayId) {
        adminClient.deleteTopics(getTopics(gatewayId));
    }

    public String getConsumerTopic(long gatewayId) {
        return "g" + gatewayId + "-from";
    }

    public String getProducerTopic(long gatewayId) {
        return "g" + gatewayId + "-to";
    }

    private Collection<String> getTopics(long gatewayId) {
        return Arrays.asList(getConsumerTopic(gatewayId), getProducerTopic(gatewayId));
    }
}
