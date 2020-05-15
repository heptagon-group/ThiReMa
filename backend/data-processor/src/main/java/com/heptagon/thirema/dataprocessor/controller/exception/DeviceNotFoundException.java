package com.heptagon.thirema.dataprocessor.controller.exception;

import javax.persistence.EntityNotFoundException;

public class DeviceNotFoundException extends EntityNotFoundException {

    public DeviceNotFoundException(){
        super("device not found");
    }
}