package com.heptagon.frontendcontroller.repository;

import com.heptagon.thirema.commons.domain.Measure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasureRepository extends CrudRepository<Measure, Long> {

    Iterable<Measure> findAllByDeviceId(Long deviceId);

    boolean existsByNameAndDeviceId(String name, Long deviceId);
}
