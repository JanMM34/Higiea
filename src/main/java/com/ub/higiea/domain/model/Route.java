package com.ub.higiea.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document("route")
public class Route {

    @Id
    private String id;

    @Field("truck_id")
    private Long truckId;

    @Field("sensor_ids")
    private List<Long> sensorIds;

    @Field("distance")
    private Double totalDistance;

    @Field("estimated_time")
    private Double estimatedTime;

    private Route() {
    }

    private Route(Long truckId, List<Long> sensorIds, Double totalDistance, Double estimatedTime) {
        this.truckId = truckId;
        this.sensorIds = sensorIds;
        this.totalDistance = totalDistance;
        this.estimatedTime = estimatedTime;
    }

    public static Route create(Long truckId, List<Long> sensorIds, Double totalDistance, Double estimatedTime) {
        return new Route(truckId, sensorIds, totalDistance, estimatedTime);
    }


    public String getId() {
        return id;
    }

    public Long getTruckId() {
        return truckId;
    }

    public List<Long> getSensorIds() {
        return sensorIds;
    }

    public Double getTotalDistance() {
        return totalDistance;
    }

    public Double getEstimatedTime() {
        return estimatedTime;
    }

}