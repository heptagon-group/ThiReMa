package com.heptagon.frontendcontroller.controller.exception;

import javax.persistence.EntityNotFoundException;

public class MeasureNotFoundException extends EntityNotFoundException {

    public MeasureNotFoundException(){
        super("configuration not found");
    }
}
