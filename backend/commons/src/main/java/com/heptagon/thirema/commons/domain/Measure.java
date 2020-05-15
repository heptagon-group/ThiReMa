package com.heptagon.thirema.commons.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "measure_config")
public class Measure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "format", nullable = false)
    private String format;

    @Column(name = "threshold", nullable = false)
    private Double threshold;

    @Column(name = "threshold_greater", nullable = false)
    private Boolean thresholdGreater;

    @Column(name = "influential", nullable = false)
    private Boolean influential;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;
}
