package com.heptagon.frontendcontroller.service;

import com.heptagon.frontendcontroller.controller.exception.AlreadyExistsException;
import com.heptagon.frontendcontroller.controller.exception.DeviceNotFoundException;
import com.heptagon.frontendcontroller.controller.exception.MeasureNotFoundException;
import com.heptagon.frontendcontroller.kafka.event.EventPublisher;
import com.heptagon.frontendcontroller.repository.DeviceRepository;
import com.heptagon.frontendcontroller.repository.MeasureRepository;
import com.heptagon.frontendcontroller.util.CustomBeanUtils;
import com.heptagon.thirema.commons.domain.Measure;
import com.heptagon.thirema.commons.event.EntityEvent;
import com.heptagon.thirema.commons.event.EntityState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MeasureService {

    private final MeasureRepository measureRepository;
    private final DeviceRepository deviceRepository;
    private final EventPublisher eventPublisher;

    public List<Measure> getMeasureList(long deviceId, long ownerId) {
        if (checkDeviceExists(deviceId, ownerId)) {
            return (List<Measure>) measureRepository.findAllByDeviceId(deviceId);
        } else {
            throw new DeviceNotFoundException();
        }
    }

    public void addMeasure(Measure measure) {
        if (checkDeviceExists(measure.getDevice().getId(), measure.getDevice().getOwnerId())) {
            checkMeasureAlreadyExists(measure, measure.getDevice().getId());
            Measure saved = measureRepository.save(measure);
            publishMeasureEvent(saved, EntityState.CREATED);
        } else {
            throw new DeviceNotFoundException();
        }
    }

    public void updateMeasure(Measure measure) {
        if (checkDeviceExists(measure.getDevice().getId(), measure.getDevice().getOwnerId())) {
            Optional<Measure> opt = measureRepository.findById(measure.getId());
            if (opt.isPresent()) {
                Measure oldMeasure = opt.get();
                checkMeasureAlreadyExists(measure, measure.getDevice().getId(), oldMeasure.getName().equals(measure.getName()));
                try {
                    CustomBeanUtils.copyPropertiesNotNull(measure, oldMeasure);
                } catch (InvocationTargetException | IllegalAccessException ignored) {
                    // non accadrà mai perché sono oggetti dello stesso tipo
                }
                Measure saved = measureRepository.save(oldMeasure);
                publishMeasureEvent(saved, EntityState.UPDATED);
            } else {
                throw new MeasureNotFoundException();
            }
        } else {
            throw new DeviceNotFoundException();
        }
    }

    public void deleteMeasure(long deviceId, long ownerId, long measureId) {
        if (checkDeviceExists(deviceId, ownerId)) {
            Optional<Measure> opt = measureRepository.findById(measureId);
            if (opt.isPresent()) {
                measureRepository.deleteById(measureId);
                publishMeasureEvent(opt.get(), EntityState.DELETED);
            } else {
                throw new MeasureNotFoundException();
            }
        } else {
            throw new DeviceNotFoundException();
        }
    }

    boolean checkDeviceExists(long deviceId, long ownerId) {
        return deviceRepository.existsByIdAndOwnerId(deviceId, ownerId);
    }

    private void checkMeasureAlreadyExists(Measure measure, long deviceId, boolean sameName) {
        List<String> fields = new ArrayList<>();
        if (!sameName && measureRepository.existsByNameAndDeviceId(measure.getName(), deviceId)) {
            fields.add("name");
        }
        if (!fields.isEmpty()) {
            throw new AlreadyExistsException(fields);
        }
    }

    private void checkMeasureAlreadyExists(Measure measure, long deviceId) {
        checkMeasureAlreadyExists(measure, deviceId,  false);
    }

    public void publishMeasureEvent(Measure measure, EntityState state) {
        EntityEvent<Measure> event = new EntityEvent<>(measure, state);
        eventPublisher.publishEvent(EntityEvent.MEASURE_TOPIC, event);
    }
}
