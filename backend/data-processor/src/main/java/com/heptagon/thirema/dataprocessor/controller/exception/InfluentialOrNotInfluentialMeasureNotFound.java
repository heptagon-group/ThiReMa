package com.heptagon.thirema.dataprocessor.controller.exception;

import javax.persistence.EntityNotFoundException;

public class InfluentialOrNotInfluentialMeasureNotFound extends EntityNotFoundException {

    public InfluentialOrNotInfluentialMeasureNotFound() {
        super("influential or not influential measure not found");
    }
}
