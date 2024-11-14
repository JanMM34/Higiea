package com.ub.higiea.infrastructure.persistence.mapper;

import com.ub.higiea.domain.model.Truck;
import com.ub.higiea.infrastructure.persistence.entities.TruckEntity;

public class TruckMapper {

    public static Truck toDomain(TruckEntity truckEntity) {
        return Truck.create(truckEntity.getId());
    }

    public static TruckEntity toEntity(Truck truck) {
        TruckEntity entity = new TruckEntity();
        if (truck.getId() != null) {
            entity.setId(truck.getId());
        }
        return entity;
    }

}
