package com.ub.higiea.domain.model;

import java.util.UUID;

public class Sensor {

    private UUID id;

    private Location location;

    private ContainerState containerState;

    private boolean assignedToRoute;

    private Sensor() {
    }

    private Sensor(UUID id, Location location, ContainerState containerState) {
        this.id = id;
        this.location = location;
        this.containerState = containerState;
        this.assignedToRoute = false;
    }

    public static Sensor create(UUID id, Location location, ContainerState containerState) {
        return new Sensor(id, location, containerState);
    }

    public UUID getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public ContainerState getContainerState() {
        return containerState;
    }

    public void setContainerState(ContainerState containerState) {
        this.containerState = containerState;
    }

    public boolean isAssignedToRoute() {
        return assignedToRoute;
    }

    public void markAssignedToRoute() {
        this.assignedToRoute = true;
    }

    public void markUnassignedFromRoute() {
        this.assignedToRoute = false;
    }

}