package com.ub.higiea.infrastructure.persistence.repositories;

import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.infrastructure.persistence.entities.SensorEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface SensorEntityRepository extends ReactiveCrudRepository<SensorEntity, UUID> {

    @Query("SELECT * FROM sensor WHERE assigned_to_route = false AND state != 'EMPTY' ORDER BY state DESC LIMIT :capacity")
    Flux<SensorEntity> findRelevantSensors(int capacity);

}