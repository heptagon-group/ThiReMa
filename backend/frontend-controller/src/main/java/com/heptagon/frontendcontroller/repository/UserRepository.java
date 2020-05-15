package com.heptagon.frontendcontroller.repository;

import com.heptagon.thirema.commons.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByTelegramUsername(String telegramUsername);

    Optional<User> findByUsername(String username);
}
