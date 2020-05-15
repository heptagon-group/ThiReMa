package com.heptagon.frontendcontroller.controller;

import com.heptagon.frontendcontroller.controller.dto.DeviceDto;
import com.heptagon.frontendcontroller.security.user.UserPrincipal;
import com.heptagon.frontendcontroller.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/device")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping
    public List<DeviceDto> getDeviceList() {
        return deviceService.getDeviceList(UserPrincipal.fromContext().getId()).stream()
                .map(DeviceDto::fromDevice)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceDto addDevice(@Validated(DeviceDto.ValidationAdd.class) @RequestBody DeviceDto device) {
        return DeviceDto.fromDevice(deviceService.addDevice(device.toDevice(UserPrincipal.fromContext().getId())));
    }

    @PatchMapping("/{id}")
    public void updateDevice(@PathVariable long id,
                             @Validated(DeviceDto.ValidationUpdate.class) @RequestBody DeviceDto device) {
        deviceService.updateDevice(device.toDevice(id, UserPrincipal.fromContext().getId()));
    }

    @DeleteMapping("/{id}")
    public void deleteDevice(@PathVariable long id) {
        deviceService.deleteDevice(id, UserPrincipal.fromContext().getId());
    }

}
