package com.heptagon.thirema.gatewaysimulator.simulator;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleSupplier;

@Data
public class Device {

    private String ipAddress;
    private int frequency;
    private List<String> measures;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Map<String, DoubleSupplier> dataSupplier;
    private boolean enabled;

    public Device(String ipAddress, int frequency, List<String> measures, DoubleSupplier... suppliers) {
        this.ipAddress = ipAddress;
        this.frequency = frequency;
        this.measures = measures;
        this.dataSupplier = new HashMap<>();
        this.enabled = true;

        for (int i = 0; i < suppliers.length; i++) {
            dataSupplier.put(measures.get(i), suppliers[i]);
        }
    }

    public double getData(String measure) {
        return dataSupplier.get(measure).getAsDouble();
    }
}
