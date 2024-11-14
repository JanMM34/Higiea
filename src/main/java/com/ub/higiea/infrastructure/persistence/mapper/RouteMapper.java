package com.ub.higiea.infrastructure.persistence.mapper;

import com.ub.higiea.domain.model.Route;
import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.model.Truck;
import com.ub.higiea.infrastructure.persistence.entities.RouteEntity;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.stream.Collectors;

public class RouteMapper {

    public static Route toDomain(RouteEntity entity, Truck truck, List<Sensor> sensors) {
        return Route.create(
                entity.getId().toHexString(),
                truck,
                sensors,
                entity.getTotalDistance(),
                entity.getEstimatedTime()
        );
    }

    public static RouteEntity toEntity(Route route) {
        List<Long> sensorIds = route.getSensors().stream()
                .map(Sensor::getId)
                .collect(Collectors.toList());

        ObjectId objectId = route.getId() != null ? new ObjectId(route.getId()) : null;

        return new RouteEntity(
                objectId,
                route.getTruck().getId(),
                sensorIds,
                route.getTotalDistance(),
                route.getEstimatedTime()
        );
    }

}
