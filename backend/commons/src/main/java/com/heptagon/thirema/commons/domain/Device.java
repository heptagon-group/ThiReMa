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
@Table(name = "device")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "ip_address", unique = true, nullable = false)
    private String ipAddress;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "frequency", nullable = false)
    private Integer frequency;

    @Column(name = "gateway_id", nullable = false)
    private Long gatewayId;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;
}
