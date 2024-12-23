package com.ub.higiea.infrastructure.persistence.repositories;

import com.ub.higiea.infrastructure.persistence.entities.RouteEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface RouteEntityRepository extends ReactiveMongoRepository <RouteEntity, String> {

}