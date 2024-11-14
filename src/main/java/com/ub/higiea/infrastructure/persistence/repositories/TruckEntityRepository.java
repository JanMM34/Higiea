package com.ub.higiea.infrastructure.persistence.repositories;

import com.ub.higiea.infrastructure.persistence.entities.TruckEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TruckEntityRepository extends ReactiveCrudRepository<TruckEntity, Long> {

}