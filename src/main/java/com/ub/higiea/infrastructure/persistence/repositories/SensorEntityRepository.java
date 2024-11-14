package com.ub.higiea.infrastructure.persistence.repositories;

import com.ub.higiea.infrastructure.persistence.entities.SensorEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SensorEntityRepository extends ReactiveCrudRepository<SensorEntity, Long> {

}