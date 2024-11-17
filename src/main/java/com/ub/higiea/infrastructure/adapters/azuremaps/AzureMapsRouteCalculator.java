package com.ub.higiea.infrastructure.adapters.azuremaps;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.models.GeoPosition;
import com.azure.maps.route.MapsRouteAsyncClient;
import com.azure.maps.route.MapsRouteClientBuilder;
import com.azure.maps.route.models.*;
import com.ub.higiea.application.utils.RouteCalculationResult;
import com.ub.higiea.application.utils.RouteCalculator;
import com.ub.higiea.domain.model.Location;
import com.ub.higiea.domain.model.Sensor;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class AzureMapsRouteCalculator implements RouteCalculator {

    private final MapsRouteAsyncClient mapsRouteClient;

    public AzureMapsRouteCalculator(String azureMapsSubscriptionKey) {

        AzureKeyCredential credential = new AzureKeyCredential(azureMapsSubscriptionKey);

        MapsRouteClientBuilder builder = new MapsRouteClientBuilder()
                .credential(credential);

        this.mapsRouteClient = builder.buildAsyncClient();
    }

    @Override
    public Mono<RouteCalculationResult> calculateRoute(List<Sensor> sensors) {
        return calculateOptimizedRoute(sensors)
                .map(routeDirections -> {

                    List<RouteOptimizedWaypoint> optimizedWaypoints = routeDirections.getOptimizedWaypoints();

                    List<Sensor> orderedSensors = optimizedWaypoints.stream()
                            .sorted(Comparator.comparingInt(RouteOptimizedWaypoint::getOptimizedIndex))
                            .map(waypoint -> sensors.get(waypoint.getProvidedIndex()))
                            .collect(Collectors.toList());

                    Double totalDistance = routeDirections.getRoutes().getFirst().getSummary()
                            .getLengthInMeters().doubleValue();

                    Long estimatedTimeInSeconds = routeDirections.getRoutes().getFirst().getSummary()
                            .getTravelTimeInSeconds().getSeconds();

                    List<GeoPosition> routeGeometryPoints = routeDirections.getRoutes().getFirst()
                            .getLegs().stream()
                            .flatMap(leg -> leg.getPoints().stream())
                            .toList();

                    List<Location> routeGeometry = routeGeometryPoints.stream()
                            .map(geoPosition -> Location.create(geoPosition.getLatitude(), geoPosition.getLongitude()))
                            .collect(Collectors.toList());

                    return new RouteCalculationResult(orderedSensors, totalDistance, estimatedTimeInSeconds, routeGeometry);
                });
    }

    private Mono<RouteDirections> calculateOptimizedRoute(List<Sensor> sensors) {
        List<GeoPosition> coordinates = sensors.stream()
                .map(sensor -> new GeoPosition(
                        sensor.getLocation().getLongitude(),
                        sensor.getLocation().getLatitude()))
                .collect(Collectors.toList());

        RouteDirectionsOptions options = new RouteDirectionsOptions(coordinates)
                .setComputeBestWaypointOrder(true)
                .setRouteType(RouteType.ECONOMY);

        return mapsRouteClient.getRouteDirections(options)
                .flatMap(response -> {
                    if (response.getRoutes() == null || response.getRoutes().isEmpty()) {
                        return Mono.error(new RuntimeException("No route found"));
                    }
                    return Mono.just(response);
                });
    }
}
