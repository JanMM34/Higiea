package com.ub.higiea.infrastructure.persistence.mapper;

import org.bson.Document;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import com.ub.higiea.domain.model.Location;
import com.ub.higiea.domain.model.Route;
import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.model.Truck;
import com.ub.higiea.infrastructure.persistence.entities.RouteEntity;
import org.bson.types.ObjectId;
import org.springframework.data.geo.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RouteMapper {

    public static RouteEntity toEntity(Route route) {
        List<Map<String, Object>> features = new ArrayList<>();

        for (Sensor sensor : route.getSensors()) {
            Map<String, Object> sensorFeature = new HashMap<>();
            sensorFeature.put("type", "Feature");

            Map<String, Object> geometry = new HashMap<>();
            geometry.put("type", "Point");
            geometry.put("coordinates", List.of(
                    sensor.getLocation().getLongitude(),
                    sensor.getLocation().getLatitude()
            ));
            sensorFeature.put("geometry", geometry);

            Map<String, Object> properties = new HashMap<>();
            properties.put("type", "Sensor");
            properties.put("id", sensor.getId());
            properties.put("containerState", sensor.getContainerState().toString());
            sensorFeature.put("properties", properties);

            features.add(sensorFeature);
        }

        Map<String, Object> routeFeature = new HashMap<>();
        routeFeature.put("type", "Feature");

        Map<String, Object> routeGeometry = new HashMap<>();
        routeGeometry.put("type", "LineString");
        routeGeometry.put("coordinates", route.getRouteGeometry().stream()
                .map(location -> List.of(location.getLongitude(), location.getLatitude()))
                .toList());
        routeFeature.put("geometry", routeGeometry);

        Map<String, Object> routeProperties = new HashMap<>();
        routeProperties.put("type", "Route");
        routeProperties.put("totalDistance", route.getTotalDistance());
        routeProperties.put("estimatedTime", route.getEstimatedTimeInSeconds());
        routeProperties.put("truckId", route.getTruck().getId());
        routeFeature.put("properties", routeProperties);

        features.add(routeFeature);

        return new RouteEntity(null, "FeatureCollection", features);
    }

    public static Route toDomain(RouteEntity entity, Truck truck, List<Sensor> sensors) {
        // Parsing GeoJSON back to Route domain is not covered here for simplicity.
        throw new UnsupportedOperationException("GeoJSON to Route mapping not implemented yet.");
    }
}
