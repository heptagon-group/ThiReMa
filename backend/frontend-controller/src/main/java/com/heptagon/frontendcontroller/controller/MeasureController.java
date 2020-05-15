package com.heptagon.frontendcontroller.controller;

import com.heptagon.frontendcontroller.controller.dto.MeasureDto;
import com.heptagon.frontendcontroller.security.user.UserPrincipal;
import com.heptagon.frontendcontroller.service.MeasureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/device/{deviceId}/config")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class MeasureController {

    private final MeasureService measureService;

    @GetMapping
    public List<MeasureDto> getMeasureList(@PathVariable long deviceId) {
        return measureService.getMeasureList(deviceId, UserPrincipal.fromContext().getId()).stream()
                .map(MeasureDto::fromMeasure)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addMeasure(@PathVariable long deviceId,
                           @Validated(MeasureDto.ValidationAdd.class) @RequestBody MeasureDto measure) {
        measureService.addMeasure(measure.toMeasure(deviceId, UserPrincipal.fromContext().getId()));
    }

    @PatchMapping("/{id}")
    public void updateMeasure(@PathVariable long deviceId, @PathVariable long id,
                              @Validated(MeasureDto.ValidationUpdate.class) @RequestBody MeasureDto measure) {
        measureService.updateMeasure(measure.toMeasure(id, deviceId, UserPrincipal.fromContext().getId()));
    }

    @DeleteMapping("/{id}")
    public void deleteMeasure(@PathVariable long deviceId, @PathVariable long id) {
        measureService.deleteMeasure(deviceId, id, UserPrincipal.fromContext().getId());
    }

}
