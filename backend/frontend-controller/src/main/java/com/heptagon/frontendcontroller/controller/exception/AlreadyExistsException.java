package com.heptagon.frontendcontroller.controller.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class AlreadyExistsException extends RuntimeException {

    private final List<String> fields;

    public AlreadyExistsException(List<String> fields) {
        super("already exists");

        this.fields = fields;
    }
}
