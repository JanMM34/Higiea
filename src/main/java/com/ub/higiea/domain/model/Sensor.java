package com.ub.higiea.domain.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("sensor")
public class Sensor {

    @Id
    @Column("id")
    private Long id;

    @NotNull
    @Min(-90)
    @Max(90)
    @Column("latitude")
    private Double latitude;

    @NotNull
    @Min(-180)
    @Max(180)
    @Column("longitude")
    private Double longitude;

    @Column("state")
    private ContainerState containerState;

    private Sensor() {
    }

    private Sensor(Double latitude, Double longitude, ContainerState containerState) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.containerState = containerState;
    }

    public static Sensor create(Double latitude, Double longitude, ContainerState containerState) {
        return new Sensor(latitude, longitude, containerState);
    }

    public Long getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public ContainerState getContainerState() {
        return containerState;
    }

}