package com.heptagon.frontendcontroller.security.jwt;

import com.heptagon.frontendcontroller.security.exception.ExpiredTokenException;
import com.heptagon.frontendcontroller.security.exception.InvalidTokenException;
import com.heptagon.frontendcontroller.security.user.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.inject.Inject;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final JwtProperties properties;

    @Override
    public boolean supports(Class<?> aClass) {
        return JwtAuthenticationToken.class.isAssignableFrom(aClass);
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails details, UsernamePasswordAuthenticationToken auth)
            throws AuthenticationException {
        // nessun ulteriore controllo necessario
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken auth)
            throws AuthenticationException {
        String token = ((JwtAuthenticationToken) auth).getToken();

        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(properties.getSecretKey()).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        }

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(
                claims.get(JwtTokenProvider.AUTHORITIES_KEY).toString());

        return new UserPrincipal(Long.valueOf(claims.getSubject()), authorities);
    }
}
