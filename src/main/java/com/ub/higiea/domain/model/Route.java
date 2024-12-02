package com.ub.higiea.domain.model;

import java.util.List;

public class Route {

    private String id;

    private Truck truck;

    private List<Sensor> sensors;

    private Double totalDistance;

    private Long estimatedTimeInSeconds;

    private List<Location> routeGeometry;

    private Route() {
    }

    private Route(String id, Truck truck, List<Sensor> sensors, Double totalDistance, Long estimatedTime,
                  List<Location> routeGeometry) {
        this.id = id;
        this.truck = truck;
        this.sensors = sensors;
        this.totalDistance = totalDistance;
        this.estimatedTimeInSeconds = estimatedTime;
        this.routeGeometry = routeGeometry;
    }

    public static Route create(String id, Truck truck, List<Sensor> sensors, Double totalDistance, Long estimatedTime,
                               List<Location> routeGeometry) {
        return new Route(id, truck, sensors, totalDistance, estimatedTime, routeGeometry);
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

    public Long getEstimatedTimeInSeconds() {
        return estimatedTimeInSeconds;
    }

    public List<Location> getRouteGeometry() {
        return routeGeometry;
    }

}