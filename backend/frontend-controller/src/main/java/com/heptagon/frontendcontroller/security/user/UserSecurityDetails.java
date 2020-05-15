package com.heptagon.frontendcontroller.security.user;

import com.heptagon.thirema.commons.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@RequiredArgsConstructor
public class UserSecurityDetails implements UserDetails {

    private final User user;

    public long getId() {
        return user.getId();
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        String authority = user.getUsername().equals("admin") ? "admin" : "user";
        return AuthorityUtils.createAuthorityList(authority);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
