package com.ub.higiea.domain.repository;

import com.ub.higiea.domain.model.Sensor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SensorRepository {

    Mono<Sensor> findById(Long id);

    Flux<Sensor> findAll();

    Mono<Sensor> save(Sensor sensor);

}
