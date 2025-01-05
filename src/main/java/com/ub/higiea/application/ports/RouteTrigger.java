package com.ub.higiea.application.ports;

import com.ub.higiea.domain.model.Sensor;
import reactor.core.publisher.Mono;

public interface RouteTrigger {
    Mono<Boolean> shouldTriggerRoute(Sensor sensor);
}