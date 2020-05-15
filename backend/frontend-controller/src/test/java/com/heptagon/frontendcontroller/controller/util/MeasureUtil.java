package com.heptagon.frontendcontroller.controller.util;

import com.heptagon.thirema.commons.domain.Device;
import com.heptagon.thirema.commons.domain.Measure;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MeasureUtil {

    public static Measure createMeasure(String value) {
        return Measure.builder()
                .id(1L)
                .name(value)
                .format(value)
                .threshold(1.0D)
                .thresholdGreater(true)
                .influential(true)
                .device(Device.builder().id(1L).ownerId(1L).build())
                .build();
    }

    public static Measure createMeasure(long id) {
        return Measure.builder()
                .id(id)
                .name("name" + id)
                .format("format" + id)
                .threshold(1.0D)
                .thresholdGreater(true)
                .influential(true)
                .device(Device.builder().id(1L).ownerId(1L).build())
                .build();
    }

    public static Measure createMeasure() {
        return createMeasure(1L);
    }

    public static Measure createNullMeasure() {
        return Measure.builder().id(1L).build();
    }

    public static Measure createEmptyMeasure() {
        return createMeasure("");
    }

    public static Measure createBlankMeasure() {
        return createMeasure(" ");
    }
}
