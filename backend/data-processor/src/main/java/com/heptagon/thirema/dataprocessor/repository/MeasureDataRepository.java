package com.heptagon.thirema.dataprocessor.repository;

import com.heptagon.thirema.commons.domain.MeasureData;
import com.heptagon.thirema.dataprocessor.domain.MeasureValueAverage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeasureDataRepository extends CrudRepository<MeasureData, MeasureData.MeasureDataId> {

    @SuppressWarnings("SpringDataRepositoryMethodReturnTypeInspection")
    @Query(value = "SELECT time_bucket_gapfill('1 hour', time) AS day, " +
            "avg(measure_data.value) AS value " +
            "FROM measure_data " +
            "WHERE time > now() - INTERVAL '1 week' AND time < now() AND measure_id = :measureId " +
            "GROUP BY day, measure_id " +
            "ORDER BY day ", nativeQuery = true)
    List<MeasureValueAverage> findAverageValuePerHourLastWeekByMeasureId(@Param("measureId") long id);
}
