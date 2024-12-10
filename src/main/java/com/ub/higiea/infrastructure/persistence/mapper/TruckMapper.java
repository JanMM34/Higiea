package com.ub.higiea.infrastructure.persistence.mapper;

import com.ub.higiea.domain.model.Location;
import com.ub.higiea.domain.model.Route;
import com.ub.higiea.domain.model.Truck;
import com.ub.higiea.infrastructure.persistence.entities.TruckEntity;

public class TruckMapper {

    public static Truck toDomain(TruckEntity truckEntity) {
        Truck truck = Truck.create(null,
                truckEntity.getPlate(),
                truckEntity.getMaxLoadCapacity(),
                Location.create(truckEntity.getDepotLatitude(),truckEntity.getDepotLongitude())
        );
        if (truckEntity.getRouteId() != null) {
            truck.assignRoute(Route.create(truckEntity.getRouteId(), null, null, null, null, null));
        }
        return truck;
    }

    public static TruckEntity toEntity(Truck truck) {
        TruckEntity entity = new TruckEntity();
        entity.setPlate(truck.getPlate());
        if (truck.hasAssignedRoute()) {
            entity.setRouteId(truck.getAssignedRoute().getId());
        }
        entity.setMaxLoadCapacity(truck.getMaxLoadCapacity());
        entity.setDepotLatitude(truck.getDepotLocation().getLatitude());
        entity.setDepotLongitude(truck.getDepotLocation().getLongitude());
        return entity;
    }

}