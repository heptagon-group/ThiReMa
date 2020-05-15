package com.heptagon.frontendcontroller.security.jwt;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;
import java.util.Base64;

@ConfigurationProperties("security.jwt")
@ConstructorBinding
@Getter
public class JwtProperties {

    private final String secretKey;
    private final long expiration;
    private final long expirationRememberMe;

    public JwtProperties(String secretKey, Duration expiration, Duration expirationRememberMe) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.expiration = expiration.toMillis();
        this.expirationRememberMe = expirationRememberMe.toMillis();
    }
}
