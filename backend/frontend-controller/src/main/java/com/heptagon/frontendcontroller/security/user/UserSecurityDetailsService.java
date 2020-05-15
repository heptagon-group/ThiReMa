package com.heptagon.frontendcontroller.security.user;

import com.heptagon.thirema.commons.domain.User;
import com.heptagon.frontendcontroller.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.inject.Inject;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserSecurityDetailsService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByUsername(username);
        if (user.isPresent()) {
            return new UserSecurityDetails(user.get());
        } else {
            throw new UsernameNotFoundException("");
        }
    }
}
