package com.ub.higiea.infrastructure.persistence.mapper;

import com.ub.higiea.domain.model.Route;
import com.ub.higiea.domain.model.Truck;
import com.ub.higiea.infrastructure.persistence.entities.TruckEntity;

public class TruckMapper {

    public static Truck toDomain(TruckEntity truckEntity) {
        Truck truck = Truck.create(truckEntity.getId());
        if (truckEntity.getRouteId() != null) {
            truck.assignRoute(Route.create(truckEntity.getRouteId(), null, null, null, null, null));
        }
        return truck;
    }

    public static TruckEntity toEntity(Truck truck) {
        TruckEntity entity = new TruckEntity();
        if (truck.getId() != null) {
            entity.setId(truck.getId());
        }
        if (truck.hasAssignedRoute()) {
            entity.setRouteId(truck.getAssignedRoute().getId());
        }
        return entity;
    }
}