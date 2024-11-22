package com.ub.higiea.infrastructure.persistence.mapper;

import com.ub.higiea.domain.model.ContainerState;
import com.ub.higiea.domain.model.Location;
import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.infrastructure.persistence.entities.SensorEntity;

public class SensorMapper {

    public static Sensor toDomain(SensorEntity entity) {
        Location location = Location.create(entity.getLatitude(), entity.getLongitude());
        ContainerState containerState = ContainerState.valueOf(entity.getContainerState());

        return Sensor.create(entity.getId(), location, containerState);
    }

    public static SensorEntity toEntity(Sensor sensor) {
        SensorEntity entity = new SensorEntity(
                sensor.getLocation().getLatitude(),
                sensor.getLocation().getLongitude(),
                sensor.getContainerState().name()
        );
        if (sensor.getId() != null) {
            entity.setId(sensor.getId());
        }
        return entity;
    }

}