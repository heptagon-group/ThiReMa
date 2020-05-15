package com.heptagon.frontendcontroller.security.controller;

import com.heptagon.frontendcontroller.security.jwt.JwtTokenProvider;
import com.heptagon.frontendcontroller.security.user.UserSecurityDetails;
import com.heptagon.frontendcontroller.security.controller.dto.LoginDto;
import com.heptagon.frontendcontroller.security.controller.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class AuthenticationController {

    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;

    @PostMapping
    public TokenDto authenticate(@Valid @RequestBody LoginDto login) {
        Authentication auth =
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authManager.authenticate(auth));
        UserSecurityDetails loggedUser = (UserSecurityDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        String token = tokenProvider.createToken(loggedUser.getId(), loggedUser.getAuthorities(),
                login.getRememberMe());
        return new TokenDto(token);
    }
}
