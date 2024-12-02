package com.ub.higiea.application.requests;

import com.ub.higiea.domain.model.ContainerState;
import jakarta.validation.constraints.*;
import org.springframework.data.geo.Point;
import org.springframework.data.relational.core.mapping.Column;

public class SensorCreateRequest {

    @NotNull(message = "Latitude cannot be null")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private Double latitude;

    @NotNull(message = "Longitude cannot be null")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private Double longitude;

    @NotNull(message = "Container state cannot be null")
    private String containerState;

    private SensorCreateRequest(Double latitude, Double longitude, String containerState) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.containerState = containerState;
    }

    public static SensorCreateRequest toRequest(Double latitude, Double longitude, String containerState) {
        return new SensorCreateRequest(latitude,longitude, containerState);
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

    @AssertTrue(message = "Container state must be a valid enum value")
    private boolean isContainerStateValid() {
        return ContainerState.isValid(containerState);
    }

}