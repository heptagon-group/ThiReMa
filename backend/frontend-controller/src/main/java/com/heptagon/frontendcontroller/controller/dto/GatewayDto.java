package com.heptagon.frontendcontroller.controller.dto;

import com.heptagon.thirema.commons.domain.Gateway;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GatewayDto {

    private Long id;

    private Long ownerId;

    public Gateway toGateway(long ownerId) {
        return Gateway.builder()
                .ownerId(ownerId)
                .build();
    }

    public static GatewayDto fromGateway(Gateway gateway) {
        return builder()
                .id(gateway.getId())
                .ownerId(gateway.getOwnerId())
                .build();
    }
}
