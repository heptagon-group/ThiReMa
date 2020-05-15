package com.heptagon.frontendcontroller.controller;

import com.heptagon.frontendcontroller.controller.dto.GatewayDto;
import com.heptagon.frontendcontroller.security.user.UserPrincipal;
import com.heptagon.frontendcontroller.service.GatewayService;
import com.heptagon.thirema.commons.domain.Gateway;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user/gateway")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GatewayController {

    private final GatewayService gatewayService;

    @GetMapping
    public List<GatewayDto> getGatewayList() {
        return gatewayService.getGatewayList(UserPrincipal.fromContext().getId()).stream()
                .map(GatewayDto::fromGateway)
                .collect(Collectors.toList());
    }

    @GetMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public GatewayDto getNewGatewayId() {
        Gateway gateway = Gateway.builder()
                .ownerId(UserPrincipal.fromContext().getId())
                .build();
        return GatewayDto.fromGateway(gatewayService.getNewGatewayId(gateway));
    }

    @DeleteMapping("{id}")
    public void deleteGateway(@PathVariable long id) {
        gatewayService.deleteGateway(id, UserPrincipal.fromContext().getId());
    }
}
