package com.ub.higiea.domain.repository;

import com.ub.higiea.domain.model.Route;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RouteRepository extends ReactiveMongoRepository<Route, String> {
}
