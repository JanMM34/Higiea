package com.ub.higiea.infrastructure.persistence.mapper;

import com.ub.higiea.domain.model.ContainerState;
import com.ub.higiea.domain.model.Location;
import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.infrastructure.persistence.entities.SensorEntity;

public class SensorMapper {

    public static Sensor toDomain(SensorEntity entity) {
        Sensor sensor = Sensor.create(
                entity.getId(),
                Location.create(entity.getLatitude(), entity.getLongitude()),
                ContainerState.valueOf(entity.getContainerState())
        );
        if (entity.isAssignedToRoute()) {
            sensor.markAssignedToRoute();
        }
        return sensor;
    }

    public static SensorEntity toEntity(Sensor sensor) {
        SensorEntity entity = new SensorEntity(
                sensor.getLocation().getLatitude(),
                sensor.getLocation().getLongitude(),
                sensor.getContainerState().toString()
        );
        if (sensor.getId() != null) {
            entity.setId(sensor.getId());
        }
        entity.setAssignedToRoute(sensor.isAssignedToRoute());
        return entity;
    }

}