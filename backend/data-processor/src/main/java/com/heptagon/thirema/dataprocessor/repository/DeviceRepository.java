package com.heptagon.thirema.dataprocessor.repository;

import com.heptagon.thirema.commons.domain.Device;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends CrudRepository<Device, Long> {

    Optional<Device> findByIpAddress(String ipAddress);
}
