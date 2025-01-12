package com.ub.higiea.application.ports;

import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.model.Truck;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

public interface RoutePlanning {
    Mono<Tuple2<Truck, List<Sensor>>> prepareRoute();
}
