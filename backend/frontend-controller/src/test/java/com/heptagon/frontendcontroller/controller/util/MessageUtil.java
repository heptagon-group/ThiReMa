package com.heptagon.frontendcontroller.controller.util;

import com.heptagon.frontendcontroller.controller.exception.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtil {

    public static final String ALREADY_EXISTS = new AlreadyExistsException(null).getMessage();
    public static final String NOT_NULL = "must not be null";
    public static final String NOT_BLANK = "must not be blank";
    public static final String NULL_OR_NOT_BLANK = "must be null or must not be blank";

    public static final String BAD_CREDENTIALS = "bad credentials";

    public static final String USER_NOT_FOUND = new UserNotFoundException().getMessage();

    public static final String DEVICE_NOT_FOUND = new DeviceNotFoundException().getMessage();

    public static final String MEASURE_NOT_FOUND = new MeasureNotFoundException().getMessage();

    public static final String GATEWAY_NOT_FOUND = new GatewayNotFoundException().getMessage();
}
