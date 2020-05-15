package com.heptagon.frontendcontroller.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heptagon.frontendcontroller.error.ApiError;
import com.heptagon.frontendcontroller.security.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authManager;
    private final ObjectMapper objectMapper;
    private final RequestMatcher pathMatcher;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (pathMatcher.matches(request)) {
            String token = resolveToken(request);
            if (token != null) {
                try {
                    Authentication auth = authManager.authenticate(new JwtAuthenticationToken(token));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } catch (AuthenticationException e) {
                    SecurityContextHolder.clearContext();
                    sendError(response, e);
                    return;
                }
            } else {
                sendError(response, new InvalidTokenException());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    private void sendError(HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        ApiError error = new ApiError(HttpStatus.UNAUTHORIZED, e.getMessage());
        response.getOutputStream().print(objectMapper.writeValueAsString(error));
    }
}
