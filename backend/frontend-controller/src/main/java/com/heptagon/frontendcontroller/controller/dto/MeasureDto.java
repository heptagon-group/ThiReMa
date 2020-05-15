package com.heptagon.frontendcontroller.controller.dto;

import com.heptagon.thirema.commons.domain.Device;
import com.heptagon.thirema.commons.domain.Measure;
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
public class MeasureDto {

    public interface ValidationAdd {}
    public interface ValidationUpdate {}

    private Long id;

    @NotBlank(groups = ValidationAdd.class)
    @NullOrNotBlank(groups = ValidationUpdate.class)
    private String name;

    @NotBlank(groups = ValidationAdd.class)
    @NullOrNotBlank(groups = ValidationUpdate.class)
    private String format;

    @NotNull(groups = ValidationAdd.class)
    private Double threshold;

    @NotNull(groups = ValidationAdd.class)
    private Boolean thresholdGreater;

    @NotNull(groups = ValidationAdd.class)
    private Boolean influential;

    public Measure toMeasure(Long measureId, long deviceId, long ownerId) {
        return Measure.builder()
                .id(measureId)
                .name(getName())
                .format(getFormat())
                .threshold(getThreshold())
                .thresholdGreater(getThresholdGreater())
                .influential(getInfluential())
                .device(Device.builder().id(deviceId).ownerId(ownerId).build())
                .build();
    }

    public Measure toMeasure(long deviceId, long ownerId) {
        return toMeasure(null, deviceId, ownerId);
    }

    public static MeasureDto fromMeasure(Measure measure) {
        return builder()
                .id(measure.getId())
                .name(measure.getName())
                .format(measure.getFormat())
                .threshold(measure.getThreshold())
                .thresholdGreater(measure.getThresholdGreater())
                .influential(measure.getInfluential())
                .build();
    }
}
