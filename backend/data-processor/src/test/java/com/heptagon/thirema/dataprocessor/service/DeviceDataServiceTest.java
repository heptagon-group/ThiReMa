package com.heptagon.thirema.dataprocessor.service;

import com.heptagon.thirema.commons.domain.Measure;
import com.heptagon.thirema.dataprocessor.controller.exception.DeviceNotFoundException;
import com.heptagon.thirema.dataprocessor.controller.exception.MeasureNotFoundException;
import com.heptagon.thirema.dataprocessor.repository.DeviceRepository;
import com.heptagon.thirema.dataprocessor.repository.MeasureDataRepository;
import com.heptagon.thirema.dataprocessor.repository.MeasureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static com.heptagon.thirema.dataprocessor.controller.util.MessageUtil.DEVICE_NOT_FOUND;
import static com.heptagon.thirema.dataprocessor.controller.util.MessageUtil.MEASURE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DeviceDataService.class})
class DeviceDataServiceTest {

    @Inject
    DeviceDataService deviceDataService;

    @MockBean
    MeasureDataRepository measureDataRepository;

    @MockBean
    DeviceRepository deviceRepository;

    @MockBean
    MeasureRepository measureRepository;

    @Test
    public void getDeviceData_deviceDoesNotExist_deviceNotFound() {
        assertThrows(DeviceNotFoundException.class, () -> deviceDataService.getDeviceData(1L), DEVICE_NOT_FOUND);
    }

    @Test
    public void getDeviceData_measureListIsEmpty_measureNotFound() {
        List<Measure> measureList =  Collections.emptyList();

        when(deviceRepository.existsById(anyLong())).thenReturn(true);
        when(measureRepository.findAllByDeviceId(anyLong())).thenReturn(measureList);

        assertThrows(MeasureNotFoundException.class, () -> deviceDataService.getDeviceData(1L), MEASURE_NOT_FOUND);
    }
}