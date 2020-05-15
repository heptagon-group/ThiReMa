package com.heptagon.thirema.commons.event;

import com.heptagon.thirema.commons.domain.Device;
import com.heptagon.thirema.commons.domain.Measure;
import com.heptagon.thirema.commons.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThresholdExceededEvent {

    public static final String TOPIC = "threshold-event";

    private User user;
    private Device device;
    private Measure measure;
    private double measureValue;
}
