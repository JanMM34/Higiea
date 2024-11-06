package com.ub.higiea.application.DTOs;

import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.model.ContainerState;
import org.springframework.data.geo.Point;
import java.io.Serializable;
import java.util.Objects;

public class SensorDTO implements Serializable {

    private Long id;
    private Point location;
    private ContainerState containerState;

    public SensorDTO() {}

    private SensorDTO(Long id, Point location, ContainerState containerState) {
        this.id = id;
        this.location = location;
        this.containerState = containerState;
    }

    public static SensorDTO fromData(Long id, Double latitude, Double longitude, ContainerState containerState) {
        return new SensorDTO(id, new Point(longitude, latitude), containerState);
    }

    public static SensorDTO toObject(Sensor sensor) {
        return new SensorDTO(
                sensor.getId(),
                new Point(sensor.getLongitude(), sensor.getLatitude()),
                sensor.getContainerState()
        );
    }

    public Long getId() {
        return id;
    }

    public Point getLocation() {
        return location;
    }

    public ContainerState getContainerState() {
        return containerState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SensorDTO sensorDTO = (SensorDTO) o;
        return Objects.equals(id, sensorDTO.id) &&
                Objects.equals(location, sensorDTO.location) &&
                containerState == sensorDTO.containerState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, location, containerState);
    }

}
