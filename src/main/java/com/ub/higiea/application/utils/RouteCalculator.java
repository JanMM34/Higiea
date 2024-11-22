package com.ub.higiea.application.utils;

import com.ub.higiea.domain.model.Sensor;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RouteCalculator {

    Mono<RouteCalculationResult> calculateRoute(List<Sensor> sensors);

}
