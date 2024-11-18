package com.ub.higiea.infrastructure.persistence.mapper;

import com.ub.higiea.domain.model.Location;
import com.ub.higiea.domain.model.Route;
import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.model.Truck;
import com.ub.higiea.infrastructure.persistence.entities.RouteEntity;
import org.bson.types.ObjectId;
import org.springframework.data.geo.Point;

import java.util.List;
import java.util.stream.Collectors;

public class RouteMapper {

    public static Route toDomain(RouteEntity entity, Truck truck, List<Sensor> sensors) {
        List<Location> locations = entity.getRouteGeometry().stream()
                .map(point ->
                        Location.create(point.getY(), point.getX())
                ).toList();

        return Route.create(
                entity.getId().toHexString(),
                truck,
                sensors,
                entity.getTotalDistance(),
                entity.getEstimatedTimeInSeconds(),
                locations
        );
    }

    public static RouteEntity toEntity(Route route) {
        List<Long> sensorIds = route.getSensors().stream()
                .map(Sensor::getId)
                .collect(Collectors.toList());

        List<Point> points = route.getRouteGeometry().stream()
                .map(location -> new Point(location.getLongitude(), location.getLatitude()))
                .collect(Collectors.toList());

        return new RouteEntity(
                null,
                route.getTruck().getId(),
                sensorIds,
                route.getTotalDistance(),
                route.getEstimatedTimeInSeconds(),
                points
        );
    }
}
