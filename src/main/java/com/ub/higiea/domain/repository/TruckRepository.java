package com.ub.higiea.domain.repository;

import com.ub.higiea.domain.model.Truck;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TruckRepository extends ReactiveCrudRepository<Truck, Long> {
}
