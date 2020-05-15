package com.heptagon.thirema.dataprocessor.kafka.gateway;

import com.heptagon.thirema.commons.domain.Device;
import com.heptagon.thirema.commons.domain.Measure;
import com.heptagon.thirema.commons.domain.MeasureData;
import com.heptagon.thirema.commons.domain.User;
import com.heptagon.thirema.commons.event.ThresholdExceededEvent;
import com.heptagon.thirema.dataprocessor.kafka.event.EventPublisher;
import com.heptagon.thirema.dataprocessor.kafka.gateway.avro.AvroDeviceData;
import com.heptagon.thirema.dataprocessor.kafka.gateway.avro.AvroMeasureData;
import com.heptagon.thirema.dataprocessor.repository.DeviceRepository;
import com.heptagon.thirema.dataprocessor.repository.MeasureDataRepository;
import com.heptagon.thirema.dataprocessor.repository.MeasureRepository;
import com.heptagon.thirema.dataprocessor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.MessageListener;

import java.sql.Timestamp;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class GatewayReceiverMessageListener implements MessageListener<String, AvroDeviceData> {

    private final MeasureDataRepository measureDataRepository;
    private final MeasureRepository measureRepository;
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;

    @Override
    public void onMessage(ConsumerRecord<String, AvroDeviceData> record) {
        AvroDeviceData deviceData = record.value();
        log.info("Received data from gateway: " + deviceData);
        Optional<Device> device = deviceRepository.findByIpAddress(deviceData.getIpAddress().toString());
        if (!device.isPresent()) {
            return;
        }

        for (AvroMeasureData measureData : deviceData.getMeasures()) {
            Optional<Measure> measure = measureRepository.findByIpAddressAndName(
                    deviceData.getIpAddress().toString(), measureData.getName().toString());
            if (!measure.isPresent()) {
                continue;
            }

            checkThreshold(measureData, measure.get(), device.get());
            saveData(measureData, measure.get());
        }
    }

    private void saveData(AvroMeasureData measureData, Measure measure) {
        MeasureData data = MeasureData.builder()
                .id(MeasureData.MeasureDataId.builder()
                        .measureId(measure.getId())
                        .time(new Timestamp(System.currentTimeMillis()))
                        .build()
                )
                .value(measureData.getValue())
                .build();
        measureDataRepository.save(data);
    }

    private void checkThreshold(AvroMeasureData measureData, Measure measure, Device device) {
        if ((measure.getThresholdGreater() && measureData.getValue() > measure.getThreshold())
                || (!measure.getThresholdGreater() && measureData.getValue() < measure.getThreshold())) {
            publishThresholdExceededEvent(device, measure, measureData.getValue());
        }
    }

    private void publishThresholdExceededEvent(Device device, Measure measure,
                                               double measureValue) {
        @SuppressWarnings("OptionalGetWithoutIsPresent") // ownerId non può essere null
        User user = userRepository.findById(device.getOwnerId()).get();
        ThresholdExceededEvent event = new ThresholdExceededEvent(user, device, measure, measureValue);
        eventPublisher.publishEvent(ThresholdExceededEvent.TOPIC, event);
    }
}