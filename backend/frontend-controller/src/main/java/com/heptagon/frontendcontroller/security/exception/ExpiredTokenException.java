package com.heptagon.frontendcontroller.security.exception;

import org.springframework.security.core.AuthenticationException;

public class ExpiredTokenException extends AuthenticationException {

    public ExpiredTokenException() {
        super("expired token");
    }
}
