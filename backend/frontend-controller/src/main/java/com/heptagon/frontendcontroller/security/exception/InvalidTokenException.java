package com.heptagon.frontendcontroller.security.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidTokenException extends AuthenticationException {

    public InvalidTokenException() {
        super("invalid token");
    }
}
