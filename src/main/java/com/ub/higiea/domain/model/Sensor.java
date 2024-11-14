package com.ub.higiea.domain.model;

public class Sensor {

    private Long id;

    private Location location;

    private ContainerState containerState;

    private Sensor() {
    }

    private Sensor(Long id, Location location, ContainerState containerState) {
        this.id = id;
        this.location = location;
        this.containerState = containerState;
    }

    public static Sensor create(Long id, Location location, ContainerState containerState) {
        return new Sensor(id, location, containerState);
    }

    public Long getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public ContainerState getContainerState() {
        return containerState;
    }

}