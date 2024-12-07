package com.ub.higiea.application.requests;

import com.ub.higiea.domain.model.Truck;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class RouteCreateRequest {

    @NotNull(message = "Sensors cannot be null")
    private Long truckId;

    @NotNull(message = "Sensors cannot be null")
    private List<Long> sensorIds;

    public RouteCreateRequest() {}

    public RouteCreateRequest(Long truckId, List<Long> sensorIds) {
        this.truckId= truckId;
        this.sensorIds = sensorIds;
    }

    public static RouteCreateRequest toRequest(Long truckId, List<Long> sensorIds) {
        return new RouteCreateRequest(truckId, sensorIds);
    }

    public List<Long> getSensorIds() {
        return sensorIds;
    }

    public Long getTruckId() {
        return truckId;
    }

}
