package com.heptagon.thirema.gatewaysimulator.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties("gateway")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
public class GatewayProperties {

    private final int id;
}
