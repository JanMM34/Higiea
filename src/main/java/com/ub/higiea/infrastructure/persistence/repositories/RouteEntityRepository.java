package com.ub.higiea.infrastructure.persistence.repositories;

import com.ub.higiea.infrastructure.persistence.entities.RouteEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface RouteEntityRepository extends ReactiveMongoRepository <RouteEntity, String> {

}