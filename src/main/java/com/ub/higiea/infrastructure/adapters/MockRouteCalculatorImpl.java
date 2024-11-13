package com.ub.higiea.infrastructure.adapters;

import com.ub.higiea.application.utils.RouteCalculator;
import com.ub.higiea.domain.model.Sensor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class MockRouteCalculatorImpl implements RouteCalculator {

    @Override
    public Mono<List<Sensor>> calculateRoute(List<Sensor> sensors) {
        return Mono.just(sensors);
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
