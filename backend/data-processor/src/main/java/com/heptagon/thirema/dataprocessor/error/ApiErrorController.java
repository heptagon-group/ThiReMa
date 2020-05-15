package com.heptagon.thirema.dataprocessor.error;

import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ApiErrorController extends AbstractErrorController {

    private static final String ERROR_PATH = "/error";

    public ApiErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @Inject
    public ApiErrorController(ErrorAttributes errorAttributes, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorViewResolvers);
    }

    @RequestMapping(ApiErrorController.ERROR_PATH)
    public ResponseEntity<ApiError> error(HttpServletRequest request) {
        ApiError error = ApiError.fromDefaultAttributeMap(getErrorAttributes(request, false));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(error, headers, error.getStatus());
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
