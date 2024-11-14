package com.ub.higiea.domain.model;

import java.util.List;

public class Route {

    private String id;

    private Truck truck;

    private List<Sensor> sensors;

    private Double totalDistance;

    private Double estimatedTime;

    private Route() {
    }

    private Route(String id, Truck truck, List<Sensor> sensors, Double totalDistance, Double estimatedTime) {
        this.id = id;
        this.truck = truck;
        this.sensors = sensors;
        this.totalDistance = totalDistance;
        this.estimatedTime = estimatedTime;
    }

    public static Route create(String id, Truck truck, List<Sensor> sensors, Double totalDistance, Double estimatedTime) {
        return new Route(id, truck, sensors, totalDistance, estimatedTime);
    }

    public String getId() {
        return id;
    }

    public Truck getTruck() {
        return truck;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public Double getEstimatedTime() {
        return estimatedTime;
    }

}