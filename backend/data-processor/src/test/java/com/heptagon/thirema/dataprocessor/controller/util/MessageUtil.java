package com.heptagon.thirema.dataprocessor.controller.util;

import com.heptagon.thirema.dataprocessor.controller.exception.DeviceNotFoundException;
import com.heptagon.thirema.dataprocessor.controller.exception.InfluentialOrNotInfluentialMeasureNotFound;
import com.heptagon.thirema.dataprocessor.controller.exception.MeasureNotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtil {

    public static final String DEVICE_NOT_FOUND = new DeviceNotFoundException().getMessage();

    public static final String MEASURE_NOT_FOUND = new MeasureNotFoundException().getMessage();

    public static final String INFLUENTIAL_OR_NOT_INFLUENTIAL_MEASURE_NOT_FOUND =
            new InfluentialOrNotInfluentialMeasureNotFound().getMessage();

}
