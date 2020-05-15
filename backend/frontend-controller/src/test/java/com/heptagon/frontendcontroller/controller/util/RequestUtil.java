package com.heptagon.frontendcontroller.controller.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtil {

    public static MockHttpServletRequestBuilder requestBody(MockHttpServletRequestBuilder request, String json) {
        return request
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);
    }
}
