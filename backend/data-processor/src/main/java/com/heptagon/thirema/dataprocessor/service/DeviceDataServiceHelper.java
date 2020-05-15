package com.heptagon.thirema.dataprocessor.service;

import com.heptagon.thirema.commons.domain.Measure;
import com.heptagon.thirema.dataprocessor.controller.dto.DeviceDataDto;
import com.heptagon.thirema.dataprocessor.controller.exception.InfluentialOrNotInfluentialMeasureNotFound;
import com.heptagon.thirema.dataprocessor.domain.MeasureValueAverage;
import com.heptagon.thirema.dataprocessor.repository.MeasureDataRepository;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class DeviceDataServiceHelper {

    private final MeasureDataRepository measureDataRepository;
    private final List<Measure> measures;
    private final List<String> measureNames;
    private final List<Double> measureThresholds;
    private final List<List<Double>> measureValues;
    private final Map<Long, List<Double>> measureValuesById;
    private final List<List<Timestamp>> measureTimes;
    private final List<List<Double>> xValuesCorrelation;
    private final List<List<Double>> yValuesCorrelation;
    private final List<List<String>> measureNamesCorrelation;

    public DeviceDataServiceHelper(MeasureDataRepository measureDataRepository, List<Measure> measures) {
        this.measureDataRepository = measureDataRepository;
        this.measures = measures;
        this.measureNames = new ArrayList<>(measures.size());
        this.measureThresholds = new ArrayList<>(measures.size());
        this.measureValues = new ArrayList<>(measures.size());
        this.measureValuesById = new HashMap<>(measures.size());
        this.measureTimes = new ArrayList<>(measures.size());
        this.xValuesCorrelation = new ArrayList<>();
        this.yValuesCorrelation = new ArrayList<>();
        this.measureNamesCorrelation = new ArrayList<>();
    }

    public DeviceDataDto execute() {
        addMeasures();
        addCorrelations();
        return buildDto();
    }

    private void addMeasures() {
        for (Measure measure : measures) {
            measureNames.add(measure.getName());
            measureThresholds.add(measure.getThreshold());

            List<MeasureValueAverage> measureValueAverages = measureDataRepository
                    .findAverageValuePerHourLastWeekByMeasureId(measure.getId());
            if (measureValueAverages.isEmpty()) {
                continue;
            }

            List<Double> values = new ArrayList<>(measureValueAverages.size());
            List<Timestamp> times = new ArrayList<>(measureValueAverages.size());
            for (MeasureValueAverage average : measureValueAverages) {
                values.add(average.getValue());
                times.add(average.getDay());
            }
            measureValues.add(values);
            measureValuesById.put(measure.getId(), values);
            measureTimes.add(times);
        }
    }

    private void addCorrelations() {
        Map<Boolean, List<Measure>> partitions = measures.stream()
                .collect(Collectors.partitioningBy(Measure::getInfluential));
        List<Measure> influentialMeasures = partitions.get(true);
        List<Measure> notInfluentialMeasures = partitions.get(false);

        if (influentialMeasures.isEmpty() || notInfluentialMeasures.isEmpty()) {
            throw new InfluentialOrNotInfluentialMeasureNotFound();
        }

        for (Measure influentialMeasure : influentialMeasures) {
            List<Double> influentialData = measureValuesById.get(influentialMeasure.getId());
            if (influentialData == null) {
                continue;
            }

            for (Measure notInfluentialMeasure : notInfluentialMeasures) {
                List<Double> notInfluentialData = measureValuesById.get(notInfluentialMeasure.getId());
                if (notInfluentialData == null || influentialData.size() != notInfluentialData.size()) {
                    continue;
                }

                double correlation = getCorrelation(influentialData, notInfluentialData);
                if (correlation < -0.5D || correlation > 0.5D) {
                    xValuesCorrelation.add(influentialData);
                    yValuesCorrelation.add(notInfluentialData);
                    measureNamesCorrelation.add(Arrays.asList(
                            influentialMeasure.getName(), notInfluentialMeasure.getName()));
                }
            }
        }
    }

    private double getCorrelation(List<Double> influential, List<Double> notInfluential) {
        double[] influentialPrimitive = influential.stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue).toArray();
        double[] notInfluentialPrimitive = notInfluential.stream()
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue).toArray();

        PearsonsCorrelation pearson = new PearsonsCorrelation();
        return pearson.correlation(influentialPrimitive, notInfluentialPrimitive);
    }

    private DeviceDataDto buildDto() {
        return DeviceDataDto.builder()
                .measureName(measureNames)
                .measureValue(measureValues)
                .time(measureTimes)
                .threshold(measureThresholds)
                .xValueCorrelation(xValuesCorrelation)
                .yValueCorrelation(yValuesCorrelation)
                .measureNameCorrelation(measureNamesCorrelation)
                .build();
    }
}
