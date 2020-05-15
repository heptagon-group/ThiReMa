package com.heptagon.thirema.commons.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "measure_data")
public class MeasureData {

    @EmbeddedId
    private MeasureDataId id;

    @Column(name = "value", nullable = false)
    private double value;


    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class MeasureDataId implements Serializable {

        @Column(name = "time", nullable = false)
        private Timestamp time;

        @Column(name = "measure_id", nullable = false)
        private long measureId;
    }
}
