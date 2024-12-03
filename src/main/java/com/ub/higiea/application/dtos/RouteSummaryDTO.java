package com.ub.higiea.application.dtos;

import com.ub.higiea.domain.model.Route;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class RouteSummaryDTO implements Serializable {

    private final String id;
    private final TruckDTO truck;
    private final List<SensorDTO> sensors;

    private RouteSummaryDTO(String id, TruckDTO truck, List<SensorDTO> sensors) {
        this.id = id;
        this.truck = truck;
        this.sensors = sensors;
    }

    public static RouteSummaryDTO fromRoute(Route route) {
        return new RouteSummaryDTO(
                route.getId(),
                TruckDTO.fromTruck(route.getTruck()),
                route.getSensors().stream().map(SensorDTO::fromSensor).toList()
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteSummaryDTO that = (RouteSummaryDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(truck, that.truck) &&
                Objects.equals(sensors, that.sensors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, truck, sensors);
    }

}