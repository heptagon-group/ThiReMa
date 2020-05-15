package com.heptagon.frontendcontroller.security.jwt;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@EqualsAndHashCode(callSuper = true)
public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

    @Getter
    private final String token;

    public JwtAuthenticationToken(String token) {
        super(null, null);
        this.token = token;
    }
}
