package com.heptagon.frontendcontroller.repository;

import com.heptagon.thirema.commons.domain.Device;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Long> {

    Iterable<Device> findAllByOwnerId(Long ownerId);

    Optional<Device> findByIdAndOwnerId(Long id, Long ownerId);

    boolean existsByIdAndOwnerId(Long id, Long ownerId);

    boolean existsByIpAddress(String ipAddress);
    
}
