package com.heptagon.frontendcontroller.error.validation;

import lombok.Data;

@Data
public class FieldError {

    private final String field;
    private final String message;
}
