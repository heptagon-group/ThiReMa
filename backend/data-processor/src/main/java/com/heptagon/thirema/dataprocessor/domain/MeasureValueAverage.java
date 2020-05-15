package com.heptagon.thirema.dataprocessor.domain;

import java.sql.Timestamp;

public interface MeasureValueAverage {

    Timestamp getDay();

    Double getValue();
}