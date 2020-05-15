package com.heptagon.frontendcontroller.controller.util;

import com.heptagon.thirema.commons.domain.Gateway;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GatewayUtil {

    public static Gateway createGateway() {
        return Gateway.builder()
                .id(1L)
                .ownerId(1L)
                .build();
    }
}
