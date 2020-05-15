package com.heptagon.thirema.dataprocessor.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDataDto {

    private List<String> measureName;

    private List<List<Double>>  measureValue;

    private List<List<Timestamp>> time;

    private List<Double> threshold;

    private List<List<Double>> xValueCorrelation;

    private List<List<Double>> yValueCorrelation;

    private List<List<String>> measureNameCorrelation;

}
