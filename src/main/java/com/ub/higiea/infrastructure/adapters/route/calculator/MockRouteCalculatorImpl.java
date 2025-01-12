package com.ub.higiea.infrastructure.adapters.route.calculator;

import com.ub.higiea.application.ports.RouteCalculationResult;
import com.ub.higiea.application.ports.RouteCalculator;
import com.ub.higiea.domain.model.Location;
import com.ub.higiea.domain.model.Sensor;
import reactor.core.publisher.Mono;

import java.util.List;

public class MockRouteCalculatorImpl implements RouteCalculator {

    @Override
    public Mono<RouteCalculationResult> calculateRoute(Location depotBase,List<Sensor> sensors) {

        List<Sensor> orderedSensors = sensors.stream()
                .toList();


        double totalDistance = 10.0;
        long estimatedTimeInSeconds = 30L;


        List<Location> routeGeometry = orderedSensors.stream()
                .map(Sensor::getLocation)
                .toList();

        RouteCalculationResult result = new RouteCalculationResult(
                orderedSensors,
                totalDistance,
                estimatedTimeInSeconds,
                routeGeometry
        );

        return Mono.just(result);
    }
}
