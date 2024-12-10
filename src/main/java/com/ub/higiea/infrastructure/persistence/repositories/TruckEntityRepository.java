package com.ub.higiea.infrastructure.persistence.repositories;

import com.ub.higiea.domain.model.Truck;
import com.ub.higiea.infrastructure.persistence.entities.TruckEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface TruckEntityRepository extends ReactiveCrudRepository<TruckEntity, UUID> {

}