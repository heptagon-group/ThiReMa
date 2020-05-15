package com.heptagon.frontendcontroller.service;

import com.heptagon.frontendcontroller.controller.exception.AlreadyExistsException;
import com.heptagon.frontendcontroller.controller.exception.DeviceNotFoundException;
import com.heptagon.frontendcontroller.kafka.event.EventPublisher;
import com.heptagon.frontendcontroller.repository.DeviceRepository;
import com.heptagon.thirema.commons.domain.Device;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.heptagon.frontendcontroller.controller.util.DeviceUtil.createDevice;
import static com.heptagon.frontendcontroller.controller.util.MessageUtil.ALREADY_EXISTS;
import static com.heptagon.frontendcontroller.controller.util.MessageUtil.DEVICE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DeviceService.class})
class DeviceServiceTest {

    @Inject
    DeviceService deviceService;

    @MockBean
    private DeviceRepository deviceRepository;

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    public void getDeviceList_returnDeviceList() {
        List<Device> deviceList = Arrays.asList(createDevice(1L), createDevice(2L));
        when(deviceRepository.findAllByOwnerId(anyLong())).thenReturn(deviceList);

        List<Device> testDeviceList = deviceService.getDeviceList(1L);

        assertEquals(testDeviceList, deviceList);
    }

    @Test
    public void addDevice_returnDeviceId() {
        Device device = createDevice();
        device.setId(null);
        when(deviceRepository.save(any(Device.class))).thenReturn(createDevice());

        Device saved = deviceService.addDevice(device);

        assertEquals(1L, saved.getId());
    }


    @Test
    public void addDevice_deviceAlreadyExists_throwAlreadyExistsException() {
        Device device = createDevice();
        device.setId(null);
        when(deviceRepository.existsByIpAddress(anyString())).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> deviceService.addDevice(device), ALREADY_EXISTS);
    }

    @Test
    public void updateDevice_deviceExists_ok() {
        Device device = createDevice();
        when(deviceRepository.findByIdAndOwnerId(anyLong(), anyLong())).thenReturn(Optional.of(device));
        when(deviceRepository.save(device)).thenReturn(device);

        deviceService.updateDevice(device);

        verify(deviceRepository, times(1)).save(device);
    }

    @Test
    public void updateDevice_deviceAlreadyExists_throwAlreadyExistsException() {
        Device device = createDevice();
        when(deviceRepository.findByIdAndOwnerId(anyLong(), anyLong())).thenReturn(Optional.of(device));
        when(deviceRepository.existsByIpAddress(anyString())).thenReturn(true);

        assertThrows(AlreadyExistsException.class,
                () -> deviceService.updateDevice(createDevice(2L)), ALREADY_EXISTS);
    }

    @Test
    public void updateDevice_deviceNotFound_throwDeviceNotFound() {
        when(deviceRepository.findByIdAndOwnerId(anyLong(), anyLong())).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class,
                () -> deviceService.updateDevice(createDevice(2L)), DEVICE_NOT_FOUND);
    }

    @Test
    public void deleteDevice_deviceExists_ok() {
        when(deviceRepository.findByIdAndOwnerId(anyLong(), anyLong())).thenReturn(Optional.of(createDevice()));

        deviceService.deleteDevice(1L, 1L);

        verify(deviceRepository, times(1)).deleteById(eq(1L));
    }

    @Test
    public void deleteDevice_deviceDoesNotExist_throwDeviceNotFoundException() {
        assertThrows(DeviceNotFoundException.class,
                () -> deviceService.deleteDevice(1L, 1L), DEVICE_NOT_FOUND);
    }
}