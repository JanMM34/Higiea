package com.ub.higiea.domain.repository;

import com.ub.higiea.domain.model.Route;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RouteRepository {

    Mono<Route> findById(String id);

    Flux<Route> findAll();

    Mono<Route> save(Route route);

}
