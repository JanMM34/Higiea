package com.ub.higiea.domain.repository;

import com.ub.higiea.domain.model.Sensor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SensorRepository {

    Mono<Sensor> findById(Long id);

    Flux<Sensor> findAll();

    Mono<Sensor> save(Sensor sensor);

    Flux<Sensor> saveAll(List<Sensor> sensors);

    Flux<Sensor> findRelevantSensors(int capacity);

}
