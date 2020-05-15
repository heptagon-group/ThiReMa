package com.heptagon.thirema.dataprocessor.repository;

import com.heptagon.thirema.commons.domain.Gateway;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatewayRepository extends CrudRepository<Gateway, Long> {

    Iterable<Gateway> findAllByOwnerId(Long ownerId);
}