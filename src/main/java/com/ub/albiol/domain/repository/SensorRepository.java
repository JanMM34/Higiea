package com.ub.albiol.domain.repository;

import com.ub.albiol.domain.model.Sensor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SensorRepository extends ReactiveCrudRepository<Sensor, Long> {
    Mono<Sensor> findByLocation(String location);
}
