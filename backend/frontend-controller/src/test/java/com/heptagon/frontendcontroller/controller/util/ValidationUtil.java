package com.heptagon.frontendcontroller.controller.util;

import com.heptagon.frontendcontroller.error.validation.FieldError;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidationUtil {

    public static List<FieldError> createFieldsErrors(String[] fields, String message) {
        List<FieldError> fieldErrors = new ArrayList<>();
        for (String field : fields) {
            fieldErrors.add(new FieldError(field, message));
        }
        return fieldErrors;
    }
}
