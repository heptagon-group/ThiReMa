package com.heptagon.frontendcontroller.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class JwtTokenProvider {

    static final String AUTHORITIES_KEY = "auth";

    private final JwtProperties properties;

    public String createToken(Long id, List<GrantedAuthority> authorities, boolean rememberMe) {
        Date now = new Date();
        long expiration = rememberMe ? properties.getExpirationRememberMe(): properties.getExpiration();
        Date validity = new Date(now.getTime() + expiration);
        String authoritiesString = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(id.toString())
                .claim(AUTHORITIES_KEY, authoritiesString)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, properties.getSecretKey())
                .compact();
    }
}
