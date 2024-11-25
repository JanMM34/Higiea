package com.ub.higiea.domain.model;

public class Truck {

    private Long id;

    private Route assignedRoute;

    private Truck() {
    }

    private Truck(Long id, Route assignedRoute) {
        this.id = id;
        this.assignedRoute = assignedRoute;
    }

    public static Truck create(Long id) {
        return new Truck(id, null);
    }

    public Long getId() {
        return id;
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

}
