package com.ub.higiea.application.requests;

import com.ub.higiea.domain.model.ContainerState;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.geo.Point;
import org.springframework.data.relational.core.mapping.Column;

public class SensorCreateRequest {

    @NotNull(message = "Latitude cannot be null")
    @Min(-90)
    @Max(90)
    private Double latitude;

    @NotNull(message = "Longitude cannot be null")
    @Min(-180)
    @Max(180)
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