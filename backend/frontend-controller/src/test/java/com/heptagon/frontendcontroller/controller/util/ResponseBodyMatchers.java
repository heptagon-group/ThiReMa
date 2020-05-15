package com.heptagon.frontendcontroller.controller.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.heptagon.frontendcontroller.error.ApiError;
import com.heptagon.frontendcontroller.error.validation.FieldError;
import com.heptagon.frontendcontroller.error.validation.ValidationError;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResponseBodyMatchers {

    private final ObjectMapper objectMapper;

    private ResponseBodyMatchers() {
        JavaTimeModule module = new JavaTimeModule();
        LocalDateTimeDeserializer localDateTimeDeserializer =  new LocalDateTimeDeserializer(
                DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        module.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        objectMapper = Jackson2ObjectMapperBuilder.json()
                .modules(module, new ParameterNamesModule())
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    public ResultMatcher hasError(String expectedMessage) {
        return result -> {
            String json = result.getResponse().getContentAsString();
            ApiError error = objectMapper.readValue(json, ApiError.class);

            assertEquals(error.getMessage(), expectedMessage);
        };
    }

    public ResultMatcher containsFieldsErrors(List<FieldError> fieldErrors) {
        return result -> {
            String json = result.getResponse().getContentAsString();
            ValidationError validationError = objectMapper.readValue(json, ValidationError.class);
            assertTrue(validationError.getFieldErrors().containsAll(fieldErrors));
        };
    }

    public ResultMatcher containsFieldsErrors(List<String> expectedFieldsName, String expectedMessage) {
        List<FieldError> fieldErrors = expectedFieldsName.stream()
                .map(fieldName -> new FieldError(fieldName, expectedMessage))
                .collect(Collectors.toList());
        return containsFieldsErrors(fieldErrors);
    }

    public ResultMatcher containsFieldError(String expectedFieldName, String expectedMessage) {
        return containsFieldsErrors(Collections.singletonList(expectedFieldName), expectedMessage);
    }

    public static ResponseBodyMatchers responseBody() {
        return new ResponseBodyMatchers();
    }
}