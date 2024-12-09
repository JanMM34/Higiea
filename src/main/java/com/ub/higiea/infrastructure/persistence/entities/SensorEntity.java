package com.ub.higiea.infrastructure.persistence.entities;

import com.ub.higiea.domain.model.ContainerState;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Table("sensor")
public class SensorEntity {

    @Id
    private Long id;

    @Column("latitude")
    private Double latitude;

    @Column("longitude")
    private Double longitude;

    @Column("state")
    private String containerState;

    @Column("assigned_to_route")
    private boolean assignedToRoute;

    public SensorEntity() {
    }

    public SensorEntity(Double latitude, Double longitude, String containerState) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.containerState = containerState;
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

    public String getContainerState() {
        return containerState;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isAssignedToRoute() {
        return assignedToRoute;
    }

    public void setAssignedToRoute(boolean assignedToRoute) {
        this.assignedToRoute = assignedToRoute;
    }

}