package com.heptagon.frontendcontroller.controller.exception;

import javax.persistence.EntityNotFoundException;

public class GatewayNotFoundException extends EntityNotFoundException {

    public GatewayNotFoundException() {
        super("gateway not found");
    }
}
