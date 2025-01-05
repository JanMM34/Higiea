package com.ub.higiea.infrastructure.adapters.route.calculator;

import com.azure.core.models.GeoPosition;
import com.azure.maps.route.MapsRouteAsyncClient;
import com.azure.maps.route.models.*;
import com.ub.higiea.application.ports.RouteCalculationResult;
import com.ub.higiea.application.ports.RouteCalculator;
import com.ub.higiea.domain.model.Location;
import com.ub.higiea.domain.model.Sensor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class AzureMapsRouteCalculator implements RouteCalculator {

    private final MapsRouteAsyncClient mapsRouteClient;

    public AzureMapsRouteCalculator(MapsRouteAsyncClient mapsRouteClient) {
        this.mapsRouteClient = mapsRouteClient;
    }

    @Override
    public Mono<RouteCalculationResult> calculateRoute(Location depotBase, List<Sensor> sensors) {
        return calculateOptimizedRoute(depotBase, sensors)
                .map(routeDirections -> {

                    List<Sensor> orderedSensors;

                    List<RouteOptimizedWaypoint> optimizedWaypoints = routeDirections.getOptimizedWaypoints();

                    orderedSensors = optimizedWaypoints.stream()
                            .sorted(Comparator.comparing(RouteOptimizedWaypoint::getOptimizedIndex))
                            .map(waypoint -> {
                                Integer providedIdx = waypoint.getProvidedIndex();
                                return sensors.get(providedIdx);
                            }).toList();

                    Double totalDistance = routeDirections.getRoutes().getFirst().getSummary()
                            .getLengthInMeters().doubleValue();

                    Long estimatedTimeInSeconds = routeDirections.getRoutes().getFirst().getSummary()
                            .getTravelTimeInSeconds().getSeconds();

                    List<GeoPosition> routeGeometryPoints = routeDirections.getRoutes().getFirst()
                            .getLegs().stream()
                            .flatMap(leg -> leg.getPoints().stream())
                            .collect(Collectors.toList());

                    List<Location> routeGeometry = routeGeometryPoints.stream()
                            .map(geoPosition -> Location.create(geoPosition.getLatitude(), geoPosition.getLongitude()))
                            .collect(Collectors.toList());

                    return new RouteCalculationResult(orderedSensors, totalDistance, estimatedTimeInSeconds, routeGeometry);
                });
    }

    private Mono<RouteDirections> calculateOptimizedRoute(Location depotBase, List<Sensor> sensors) {

        List<GeoPosition> coordinates = new ArrayList<>();

        // Add depot base as start point
        coordinates.add(new GeoPosition(depotBase.getLongitude(), depotBase.getLatitude()));

        // Add sensor locations
        coordinates.addAll(sensors.stream()
                .map(sensor -> new GeoPosition(
                        sensor.getLocation().getLongitude(),
                        sensor.getLocation().getLatitude()))
                .collect(Collectors.toList()));

        // Add depot base as end point
        coordinates.add(new GeoPosition(depotBase.getLongitude(), depotBase.getLatitude()));

        RouteDirectionsOptions options = new RouteDirectionsOptions(coordinates)
                .setComputeBestWaypointOrder(true)
                .setTravelMode(TravelMode.CAR)
                .setRouteType(RouteType.SHORTEST);

        return mapsRouteClient.getRouteDirections(options)
                .flatMap(response -> {
                    if (response.getRoutes() == null || response.getRoutes().isEmpty()) {
                        return Mono.error(new RuntimeException("No route found"));
                    }
                    return Mono.just(response);
                });
    }

}