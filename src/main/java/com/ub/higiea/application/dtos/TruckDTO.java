package com.ub.higiea.application.dtos;

import com.ub.higiea.domain.model.Truck;
import java.io.Serializable;
import java.util.Objects;

public class TruckDTO implements Serializable {

    private Long id;
    private String routeId;

    public TruckDTO() {
    }

    private TruckDTO(Long id, String routeId) {
        this.id = id;
        this.routeId = routeId;
    }

    public static TruckDTO fromTruck(Truck truck) {
        String routeId = truck.hasAssignedRoute() ? truck.getAssignedRoute().getId() : null;
        return new TruckDTO(truck.getId(), routeId);
    }

    public Long getId() {
        return id;
    }

    public String getRouteId() {
        return routeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruckDTO truckDTO = (TruckDTO) o;
        return Objects.equals(this.id, truckDTO.id) &&
                Objects.equals(this.routeId, truckDTO.routeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, routeId);
    }

}
