package com.ub.higiea.infrastructure.adapters;

import com.ub.higiea.application.utils.RouteCalculationResult;
import com.ub.higiea.application.utils.RouteCalculator;
import com.ub.higiea.domain.model.Location;
import com.ub.higiea.domain.model.Sensor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MockRouteCalculatorImpl implements RouteCalculator {

    @Override
    public Mono<RouteCalculationResult> calculateRoute(List<Sensor> sensors) {

        List<Sensor> orderedSensors = sensors.stream()
                .sorted(Comparator.comparingLong(Sensor::getId))
                .collect(Collectors.toList());


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
