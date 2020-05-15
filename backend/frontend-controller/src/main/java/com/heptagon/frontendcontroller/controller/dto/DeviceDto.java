package com.heptagon.frontendcontroller.controller.dto;

import com.heptagon.thirema.commons.domain.Device;
import com.heptagon.thirema.commons.validation.constraints.NullOrNotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDto {

    public interface ValidationAdd {}
    public interface ValidationUpdate {}

    private Long id;

    @NotBlank(groups = ValidationAdd.class)
    @NullOrNotBlank(groups = ValidationUpdate.class)
    private String name;

    @NotBlank(groups = ValidationAdd.class)
    @NullOrNotBlank(groups = ValidationUpdate.class)
    private String ipAddress;

    @NotBlank(groups = ValidationAdd.class)
    @NullOrNotBlank(groups = ValidationUpdate.class)
    private String brand;

    @NotBlank(groups = ValidationAdd.class)
    @NullOrNotBlank(groups = ValidationUpdate.class)
    private String model;

    @NotNull(groups = ValidationAdd.class)
    private Integer frequency;

    @NotNull(groups = ValidationAdd.class)
    private Long gatewayId;

    public Device toDevice(Long deviceId, long ownerId) {
        return Device.builder()
                .id(deviceId)
                .name(getName())
                .ipAddress(getIpAddress())
                .brand(getBrand())
                .model(getModel())
                .frequency(getFrequency())
                .gatewayId(getGatewayId())
                .ownerId(ownerId)
                .build();
    }

    public Device toDevice(Long ownerId) {
        return toDevice(null, ownerId);
    }

    public static DeviceDto fromDevice(Device device) {
        return builder()
                .id(device.getId())
                .name(device.getName())
                .ipAddress(device.getIpAddress())
                .brand(device.getBrand())
                .model(device.getModel())
                .frequency(device.getFrequency())
                .gatewayId(device.getGatewayId())
                .build();
    }
}
