package com.ub.higiea.domain.model;

import org.springframework.data.annotation.Transient;

public class Truck {

    private Long id;

    private Route assignedRoute;

    private final int maxLoadCapacity;

    private Location depotLocation;

    private Truck(Long id, Route assignedRoute, int maxLoadCapacity, Location depotLocation) {
        this.id = id;
        this.assignedRoute = assignedRoute;
        this.maxLoadCapacity = maxLoadCapacity;
        this.depotLocation = depotLocation;
    }

    public static Truck create(Long id, int maxLoadCapacity, Location depotLocation) {
        return new Truck(id, null, maxLoadCapacity, depotLocation);
    }

    public Long getId() {
        return id;
    }

    public int getMaxLoadCapacity() {
        return maxLoadCapacity;
    }

    public Route getAssignedRoute() {
        return assignedRoute;
    }

    public void assignRoute(Route route) {
        if (this.assignedRoute != null) {
            throw new IllegalStateException("Truck is already assigned to a assignedRoute");
        }
        this.assignedRoute = route;
    }

    public void unassignRoute() {
        if (this.assignedRoute == null) {
            throw new IllegalStateException("Truck does not have an assigned route");
        }
        this.assignedRoute = null;
    }

    public boolean hasAssignedRoute() {
        return this.assignedRoute != null;
    }

    public Location getDepotLocation() {
        return depotLocation;
    }

}