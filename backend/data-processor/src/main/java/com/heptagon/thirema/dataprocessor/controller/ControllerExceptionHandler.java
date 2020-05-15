package com.heptagon.thirema.dataprocessor.controller;

import com.heptagon.thirema.dataprocessor.error.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException e) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    protected ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
