package com.heptagon.frontendcontroller.controller.exception;

import javax.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException() {
        super("user not found");
    }
}