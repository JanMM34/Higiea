package com.ub.higiea.application.utils;

import com.ub.higiea.domain.model.Sensor;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RouteCalculator {
    Mono<List<Sensor>> calculateRoute(List<Sensor> sensors);
    Mono<Double> calculateTotalDistance(List<Sensor> sensors);
    Mono<Double> calculateEstimatedTime(List<Sensor> sensors);
}
