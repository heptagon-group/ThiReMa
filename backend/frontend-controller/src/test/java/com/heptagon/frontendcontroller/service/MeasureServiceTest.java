package com.heptagon.frontendcontroller.service;

import com.heptagon.frontendcontroller.controller.exception.AlreadyExistsException;
import com.heptagon.frontendcontroller.controller.exception.DeviceNotFoundException;
import com.heptagon.frontendcontroller.controller.exception.MeasureNotFoundException;
import com.heptagon.frontendcontroller.kafka.event.EventPublisher;
import com.heptagon.frontendcontroller.repository.DeviceRepository;
import com.heptagon.frontendcontroller.repository.MeasureRepository;
import com.heptagon.thirema.commons.domain.Measure;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.heptagon.frontendcontroller.controller.util.MeasureUtil.createMeasure;
import static com.heptagon.frontendcontroller.controller.util.MessageUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MeasureService.class})
class MeasureServiceTest {

    @Inject
    private MeasureService measureService;

    @MockBean
    private MeasureRepository measureRepository;

    @MockBean
    private DeviceRepository deviceRepository;

    @MockBean
    private EventPublisher eventPublisher;

    @Test
    public void getMeasureList_deviceExists_okAndReturnList() {
        List<Measure> measureList = Arrays.asList(createMeasure(1L), createMeasure(2L));
        when(deviceRepository.existsByIdAndOwnerId(anyLong(), anyLong())).thenReturn(true);
        when(measureRepository.findAllByDeviceId(anyLong())).thenReturn(measureList);

        List<Measure> measureListTest = measureService.getMeasureList(1L, 1L);

        assertEquals(measureListTest, measureList);
    }

    @Test
    public void getMeasureList_deviceDoesNotExist_throwDeviceNotFoundException() {
        assertThrows(DeviceNotFoundException.class,
                () -> measureService.getMeasureList(1L, 1L), DEVICE_NOT_FOUND);
    }

    @Test
    public void addMeasure_deviceExistsAndMeasureDoesNotAlreadyExist_ok() {
        Measure measure = createMeasure();
        when(deviceRepository.existsByIdAndOwnerId(1L, 1L)).thenReturn(true);
        when(measureRepository.findById(1L)).thenReturn(Optional.of(measure));

        measureService.addMeasure(measure);

        verify(measureRepository, times(1)).save(measure);
    }

    @Test
    public void addMeasure_deviceExistsAndMeasureAlreadyExist_throwAlreadyExistsException() {
        Measure measure = createMeasure();
        when(deviceRepository.existsByIdAndOwnerId(1L, 1L)).thenReturn(true);
        when(measureRepository.existsByNameAndDeviceId(measure.getName(), 1L)).thenReturn(true);

        assertThrows(AlreadyExistsException.class,
                () -> measureService.addMeasure(measure), ALREADY_EXISTS);
    }

    @Test
    public void addMeasure_deviceDoesNotExist_throwDeviceNotFoundException() {
        assertThrows(DeviceNotFoundException.class,
                () -> measureService.addMeasure(createMeasure()), DEVICE_NOT_FOUND);
    }

    @Test
    public void updateMeasure_deviceDoesNotExist_throwDeviceNotFoundException() {
        assertThrows(DeviceNotFoundException.class,
                () -> measureService.updateMeasure(createMeasure()), DEVICE_NOT_FOUND);
    }

    @Test
    public void updateMeasure_deviceExistsAndMeasureAlreadyExists_throwAlreadyExistsException() {
        Measure measure = createMeasure();
        when(deviceRepository.existsByIdAndOwnerId(1L, 1L)).thenReturn(true);
        when(measureRepository.findById(1L)).thenReturn(Optional.of(createMeasure(2L)));
        when(measureRepository.existsByNameAndDeviceId(measure.getName(), 1L)).thenReturn(true);

        assertThrows(AlreadyExistsException.class,
                () -> measureService.updateMeasure(measure), ALREADY_EXISTS);
    }

    @Test
    public void updateMeasure_ok() {
        Measure measure = createMeasure();
        when(deviceRepository.existsByIdAndOwnerId(1L, 1L)).thenReturn(true);
        when(measureRepository.findById(1L)).thenReturn(Optional.of(measure));

        measureService.updateMeasure(measure);

        verify(measureRepository, times(1)).save(measure);
    }

    @Test
    public void deleteMeasure_deviceDoesNotExist_throwDeviceNotFoundException() {
        assertThrows(DeviceNotFoundException.class,
                () -> measureService.deleteMeasure(1L, 1L, 1L), DEVICE_NOT_FOUND);
    }

    @Test
    public void deleteMeasure_measureDoesNotExist_throwMeasureNotFoundException() {
        when(deviceRepository.existsByIdAndOwnerId(1L, 1L)).thenReturn(true);

        assertThrows(MeasureNotFoundException.class,
                () -> measureService.deleteMeasure(1L, 1L, 1L), MEASURE_NOT_FOUND);
    }

    @Test
    public void deleteMeasure_ok() {
        when(deviceRepository.existsByIdAndOwnerId(1L, 1L)).thenReturn(true);
        when(measureRepository.findById(1L)).thenReturn(Optional.of(createMeasure()));

        measureService.deleteMeasure(1L, 1L, 1L);

        verify(measureRepository, times(1)).deleteById(1L);
    }
}