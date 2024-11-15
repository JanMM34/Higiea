package com.ub.higiea.application.dtos;

import com.ub.higiea.domain.model.Route;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class RouteDTO implements Serializable {

    private final String id;
    private final TruckDTO truck;
    private final List<SensorDTO> sensors;
    private final Double totalDistance;
    private final Double estimatedTime;

    private RouteDTO(String id, TruckDTO truck, List<SensorDTO> sensors, Double totalDistance, Double estimatedTime) {
        this.id = id;
        this.truck = truck;
        this.sensors = sensors;
        this.totalDistance = totalDistance;
        this.estimatedTime = estimatedTime;
    }

    public static RouteDTO fromRoute(Route route) {
        return new RouteDTO(
                route.getId(),
                TruckDTO.fromTruck(route.getTruck()),
                route.getSensors().stream().map(SensorDTO::fromSensor).toList(),
                route.getTotalDistance(),
                route.getEstimatedTime()
        );
    }

    public String getId() {
        return id;
    }

    public TruckDTO getTruck() {
        return truck;
    }

    public List<SensorDTO> getSensors() {
        return sensors;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public Double getEstimatedTime() {
        return estimatedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteDTO routeDTO = (RouteDTO) o;
        return Objects.equals(id, routeDTO.id) &&
                Objects.equals(truck, routeDTO.truck) &&
                Objects.equals(sensors, routeDTO.sensors) &&
                Objects.equals(totalDistance, routeDTO.totalDistance) &&
                Objects.equals(estimatedTime, routeDTO.estimatedTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, truck, sensors, totalDistance, estimatedTime);
    }

}