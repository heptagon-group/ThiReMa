package com.heptagon.frontendcontroller.error.validation;

import com.heptagon.frontendcontroller.error.ApiError;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
public class ValidationError extends ApiError {

    private final List<FieldError> fieldErrors = new ArrayList<>();

    public ValidationError(HttpStatus status) {
        super(status, "validation error");
    }

    public ValidationError() {
        this(HttpStatus.BAD_REQUEST);
    }

    public void addFieldError(String field, String message) {
        fieldErrors.add(new FieldError(field, message));
    }
}