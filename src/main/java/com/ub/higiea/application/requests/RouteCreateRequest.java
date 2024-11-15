package com.ub.higiea.application.requests;

import java.util.List;

public class RouteCreateRequest {

    private final Long truckId;
    private final List<Long> sensorIds;

    public RouteCreateRequest(Long truckId, List<Long> sensorIds) {
        this.truckId = truckId;
        this.sensorIds = sensorIds;
    }

    public static RouteCreateRequest toRequest(Long truckId, List<Long> sensorIds) {
        return new RouteCreateRequest(truckId, sensorIds);
    }

    public Long getTruckId() {
        return truckId;
    }

    public List<Long> getSensorIds() {
        return sensorIds;
    }

}
