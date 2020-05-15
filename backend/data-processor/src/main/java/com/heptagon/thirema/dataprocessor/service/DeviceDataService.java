package com.heptagon.thirema.dataprocessor.service;

import com.heptagon.thirema.commons.domain.Measure;
import com.heptagon.thirema.dataprocessor.controller.dto.DeviceDataDto;
import com.heptagon.thirema.dataprocessor.controller.exception.DeviceNotFoundException;
import com.heptagon.thirema.dataprocessor.controller.exception.MeasureNotFoundException;
import com.heptagon.thirema.dataprocessor.repository.DeviceRepository;
import com.heptagon.thirema.dataprocessor.repository.MeasureDataRepository;
import com.heptagon.thirema.dataprocessor.repository.MeasureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DeviceDataService {

    private final MeasureRepository measureRepository;
    private final MeasureDataRepository measureDataRepository;
    private final DeviceRepository deviceRepository;

    public DeviceDataDto getDeviceData(long id) {
        if (!deviceRepository.existsById(id)) {
            throw new DeviceNotFoundException();
        }

        List<Measure> measureList = (List<Measure>) measureRepository.findAllByDeviceId(id);
        if (measureList.isEmpty()) {
            throw new MeasureNotFoundException();
        }

        return new DeviceDataServiceHelper(measureDataRepository, measureList).execute();
    }
}
