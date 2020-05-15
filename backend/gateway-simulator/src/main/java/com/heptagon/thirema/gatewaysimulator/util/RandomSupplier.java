package com.heptagon.thirema.gatewaysimulator.util;

import lombok.RequiredArgsConstructor;

import java.util.Random;
import java.util.function.DoubleSupplier;

@RequiredArgsConstructor
public class RandomSupplier implements DoubleSupplier {

    private final Random random = new Random();
    private final double min;
    private final double max;

    @Override
    public double getAsDouble() {
        return min + (max - min) * random.nextDouble();
    }
}
