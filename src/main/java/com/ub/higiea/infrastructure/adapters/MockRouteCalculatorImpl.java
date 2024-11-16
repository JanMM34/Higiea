package com.ub.higiea.infrastructure.adapters;

import com.ub.higiea.application.utils.RouteCalculator;
import com.ub.higiea.domain.model.Sensor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MockRouteCalculatorImpl implements RouteCalculator {

    @Override
    public Mono<List<Sensor>> calculateRoute(List<Sensor> sensors) {
        return Mono.just(
                sensors.stream()
                        .sorted(Comparator.comparingLong(Sensor::getId))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Mono<Double> calculateTotalDistance(List<Sensor> sensors) {
        return Mono.just(10.0);
    }

    @Override
    public Mono<Double> calculateEstimatedTime(List<Sensor> sensors) {
        return Mono.just(30.0);
    }
}
