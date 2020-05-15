package com.heptagon.frontendcontroller.service;

import com.heptagon.frontendcontroller.controller.exception.AlreadyExistsException;
import com.heptagon.frontendcontroller.controller.exception.DeviceNotFoundException;
import com.heptagon.frontendcontroller.kafka.event.EventPublisher;
import com.heptagon.frontendcontroller.repository.DeviceRepository;
import com.heptagon.frontendcontroller.util.CustomBeanUtils;
import com.heptagon.thirema.commons.domain.Device;
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
public class DeviceService {

    private final DeviceRepository deviceRepository;
    private final EventPublisher eventPublisher;

    public List<Device> getDeviceList(long ownerId) {
        return (List<Device>) deviceRepository.findAllByOwnerId(ownerId);
    }

    public Device addDevice(Device device) {
        checkDeviceAlreadyExists(device);
        Device saved = deviceRepository.save(device);
        publishDeviceEvent(saved, EntityState.CREATED);
        Device savedId = new Device();
        savedId.setId(saved.getId());
        return savedId;
    }

    public void updateDevice(Device device) {
        Optional<Device> opt = deviceRepository.findByIdAndOwnerId(device.getId(), device.getOwnerId());
        if (opt.isPresent()) {
            Device oldDevice = opt.get();
            checkDeviceAlreadyExists(device, oldDevice.getIpAddress().equals(device.getIpAddress()));
            try {
                CustomBeanUtils.copyPropertiesNotNull(device, oldDevice);
            } catch (InvocationTargetException | IllegalAccessException ignored) {
                // non accadrà mai perché sono oggetti dello stesso tipo
            }
            Device saved = deviceRepository.save(oldDevice);
            publishDeviceEvent(saved, EntityState.UPDATED);
        } else {
            throw new DeviceNotFoundException();
        }
    }

    public void deleteDevice(long deviceId, long ownerId) {
        Optional<Device> opt = deviceRepository.findByIdAndOwnerId(deviceId, ownerId);
        if (opt.isPresent()) {
            deviceRepository.deleteById(deviceId);
            publishDeviceEvent(opt.get(), EntityState.DELETED);
        } else {
            throw new DeviceNotFoundException();
        }
    }

    private void checkDeviceAlreadyExists(Device device, boolean sameIpAddress) {
        List<String> fields = new ArrayList<>();
        if (!sameIpAddress && deviceRepository.existsByIpAddress(device.getIpAddress())) {
            fields.add("ipAddress");
        }
        if (!fields.isEmpty()) {
            throw new AlreadyExistsException(fields);
        }
    }

    private void checkDeviceAlreadyExists(Device device) {
        checkDeviceAlreadyExists(device, false);
    }

    private void publishDeviceEvent(Device device, EntityState state) {
        EntityEvent<Device> event = new EntityEvent<>(device, state);
        eventPublisher.publishEvent(EntityEvent.DEVICE_TOPIC, event);
    }

}
