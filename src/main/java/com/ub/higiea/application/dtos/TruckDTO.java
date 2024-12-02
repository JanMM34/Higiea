package com.ub.higiea.application.dtos;

import com.ub.higiea.domain.model.Truck;
import org.springframework.data.geo.Point;

import java.io.Serializable;
import java.util.Objects;

public class TruckDTO implements Serializable {

    private final Long id;
    private final String routeId;
    private final int maxLoadCapacity;
    private final Point depotLocation;

    private TruckDTO(Long id, String routeId, int maxLoadCapacity, Point depotLocation) {
        this.id = id;
        this.routeId = routeId;
        this.maxLoadCapacity = maxLoadCapacity;
        this.depotLocation = depotLocation;
    }

    public static TruckDTO fromTruck(Truck truck) {
        String routeId = truck.hasAssignedRoute() ? truck.getAssignedRoute().getId() : null;

        return new TruckDTO(
                truck.getId(),
                routeId,
                truck.getMaxLoadCapacity(),
                new Point(
                        truck.getDepotLocation().getLatitude(),
                        truck.getDepotLocation().getLongitude()
                )
        );
    }

    public Long getId() {
        return id;
    }

    public String getRouteId() {
        return routeId;
    }

    public int getMaxLoadCapacity() {
        return maxLoadCapacity;
    }

    public Point getDepotLocation() {
        return depotLocation;
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
