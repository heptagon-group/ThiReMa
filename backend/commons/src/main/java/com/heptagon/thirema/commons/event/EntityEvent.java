package com.heptagon.thirema.commons.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityEvent<E> {

    public static final String USER_TOPIC = "user-event";
    public static final String GATEWAY_TOPIC = "gateway-event";
    public static final String DEVICE_TOPIC = "device-event";
    public static final String MEASURE_TOPIC = "measure-event";

    private E entity;
    private EntityState state;
}
