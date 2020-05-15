package com.heptagon.frontendcontroller.repository;

import com.heptagon.thirema.commons.domain.Gateway;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface GatewayRepository extends CrudRepository<Gateway, Long> {

        Iterable<Gateway> findAllByOwnerId(Long ownerId);

        boolean existsByIdAndOwnerId(Long id, Long ownerId);

        Optional<Gateway> findByIdAndOwnerId(Long id, Long ownerId);
}