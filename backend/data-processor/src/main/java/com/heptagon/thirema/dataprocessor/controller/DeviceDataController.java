package com.heptagon.thirema.dataprocessor.controller;

import com.heptagon.thirema.dataprocessor.controller.dto.DeviceDataDto;
import com.heptagon.thirema.dataprocessor.service.DeviceDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/user/device/{deviceId}")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DeviceDataController {

    private final DeviceDataService deviceDataService;

    @GetMapping("/data")
    public DeviceDataDto getDeviceData(@PathVariable long deviceId) {
        return deviceDataService.getDeviceData(deviceId);
    }
}