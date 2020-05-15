package com.heptagon.frontendcontroller.controller;

import com.heptagon.frontendcontroller.error.ApiError;
import com.heptagon.frontendcontroller.error.validation.ValidationError;
import com.heptagon.frontendcontroller.controller.exception.AlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ValidationError validationError = new ValidationError();
        for (org.springframework.validation.FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            validationError.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return buildResponseEntity(validationError);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    protected ResponseEntity<ApiError> handleAlreadyExistsException(AlreadyExistsException e) {
        ValidationError validationError = new ValidationError(HttpStatus.CONFLICT);
        for (String field : e.getFields()) {
            validationError.addFieldError(field, e.getMessage());
        }
        return buildResponseEntity(validationError);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException e) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    protected ResponseEntity<ApiError> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
