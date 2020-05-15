package com.heptagon.thirema.gatewaysimulator.kafka;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TopicGetter {

    private final int gatewayId;

    public String getProducerTopic() {
        return "g" + gatewayId + "-from";
    }

    public String getConsumerTopic() {
        return "g" + gatewayId + "-to";
    }
}
