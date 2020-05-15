package com.heptagon.frontendcontroller.security.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LoginDto {

    @NotBlank
    private final String username;
    @NotBlank
    private final String password;
    @NotNull
    private final Boolean rememberMe;
}
