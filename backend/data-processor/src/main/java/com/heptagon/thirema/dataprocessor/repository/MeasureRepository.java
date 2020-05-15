package com.heptagon.thirema.dataprocessor.repository;

import com.heptagon.thirema.commons.domain.Measure;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MeasureRepository extends CrudRepository<Measure, Long> {

        Iterable<Measure> findAllByDeviceId(Long deviceId);

        @Query(value = "SELECT m FROM Measure m JOIN m.device d " +
                "WHERE d.ipAddress = :ipAddress AND m.name = :name")
        Optional<Measure> findByIpAddressAndName(@Param("ipAddress") String ipAddress, @Param("name") String name);
}
