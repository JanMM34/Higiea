package com.ub.higiea.application.dtos;

import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.model.ContainerState;
import org.springframework.data.geo.Point;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class SensorDTO implements Serializable {

    private final UUID id;

    private final Point location;

    private final ContainerState containerState;

    private SensorDTO(UUID id, Point location, ContainerState containerState) {
        this.id = id;
        this.location = location;
        this.containerState = containerState;
    }

    public static SensorDTO fromSensor(Sensor sensor) {
        return new SensorDTO(
                sensor.getId(),
                new Point(sensor.getLocation().getLatitude(),sensor.getLocation().getLongitude()),
                sensor.getContainerState()
        );
    }

    public UUID getId() {
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
