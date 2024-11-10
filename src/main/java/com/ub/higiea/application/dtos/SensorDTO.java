package com.ub.higiea.application.dtos;

import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.model.ContainerState;
import org.springframework.data.geo.Point;
import java.io.Serializable;
import java.util.Objects;

public class SensorDTO implements Serializable {

    final private Long id;

    final private Point location;

    final private ContainerState containerState;

    private SensorDTO(Long id, Point location, ContainerState containerState) {
        this.id = id;
        this.location = location;
        this.containerState = containerState;
    }

    public static SensorDTO fromSensor(Sensor sensor) {
        return new SensorDTO(
                sensor.getId(),
                new Point(sensor.getLatitude(),sensor.getLongitude()),
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
