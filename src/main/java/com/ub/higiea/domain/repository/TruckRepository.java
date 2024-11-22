package com.ub.higiea.domain.repository;

import com.ub.higiea.domain.model.Truck;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TruckRepository {

    Mono<Truck> findById(Long id);

    Flux<Truck> findAll();

    Mono<Truck> save(Truck truck);

}
