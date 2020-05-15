package com.heptagon.frontendcontroller.error;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__(@JsonCreator))
public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private final LocalDateTime timestamp;
    private final HttpStatus status;
    private final String message;

    public ApiError(HttpStatus status, String message) {
        this(LocalDateTime.now(), status, message);
    }

    public ApiError(HttpStatus status) {
        this(status, status.getReasonPhrase());
    }

    public static ApiError fromDefaultAttributeMap(Map<String, Object> attributes) {
        return new ApiError(
                HttpStatus.valueOf((Integer) attributes.get("status")),
                ((String) attributes.get("message")).toLowerCase()
        );
    }
}
