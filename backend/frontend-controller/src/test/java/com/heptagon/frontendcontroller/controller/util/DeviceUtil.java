package com.heptagon.frontendcontroller.controller.util;

import com.heptagon.thirema.commons.domain.Device;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeviceUtil {

    public static Device createDevice(String value, Integer frequency, Long gatewayId) {
        return Device.builder()
                .id(1L)
                .name(value)
                .ipAddress(value)
                .brand(value)
                .model(value)
                .frequency(frequency)
                .gatewayId(gatewayId)
                .ownerId(1L)
                .build();
    }

    public static Device createDevice(String value) {
        return createDevice(value, 1, 1L);
    }

    public static Device createDevice(Long id) {
        return Device.builder()
                .id(id)
                .name("name" + id)
                .ipAddress("ipAddress" + id)
                .brand("brand" + id)
                .model("model" + id)
                .frequency(1)
                .gatewayId(1L)
                .ownerId(1L)
                .build();
    }

    public static Device createDevice() {
        return createDevice(1L);
    }

    public static Device createNullDevice() {
        return createDevice(null, null, null);
    }

    public static Device createEmptyDevice() {
        return createDevice("");
    }

    public static Device createBlankDevice() {
        return createDevice(" ");
    }
}
