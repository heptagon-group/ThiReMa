package com.heptagon.frontendcontroller.controller.util;

import com.heptagon.frontendcontroller.security.user.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser user) {
        SecurityContext ctx = SecurityContextHolder.createEmptyContext();
        List<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(user.authorities());
        UserPrincipal principal = new UserPrincipal(user.id(), authorities);
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, null, authorities);
        ctx.setAuthentication(auth);
        return ctx;
    }
}
