package com.heptagon.thirema.dataprocessor.controller.exception;

import javax.persistence.EntityNotFoundException;

public class MeasureNotFoundException extends EntityNotFoundException {

    public MeasureNotFoundException(){
        super("configuration not found");
    }
}