package com.ub.higiea.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("sensor")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sensor {

    @Id
    @Column("id")
    private Long id;

    @Column("latitude")
    private Double latitude;

    @Column("longitude")
    private Double longitude;

    @Column("state")
    private ContainerState containerState;

    private Sensor() {
    }

    private Sensor(Long id, Double latitude, Double longitude, ContainerState containerState) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.containerState = containerState;
    }

    public static Sensor create(Long id, Double latitude, Double longitude, ContainerState containerState) {
        return new Sensor(id, latitude, longitude, containerState);
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