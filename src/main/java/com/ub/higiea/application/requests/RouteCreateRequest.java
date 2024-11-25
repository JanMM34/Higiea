package com.ub.higiea.application.requests;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class RouteCreateRequest {

    @NotNull(message = "Sensors cannot be null")
    private List<Long> sensorIds;

    public RouteCreateRequest() {}

    public RouteCreateRequest(List<Long> sensorIds) {
        this.sensorIds = sensorIds;
    }

    public static RouteCreateRequest toRequest(List<Long> sensorIds) {
        return new RouteCreateRequest(sensorIds);
    }

    public List<Long> getSensorIds() {
        return sensorIds;
    }

}
