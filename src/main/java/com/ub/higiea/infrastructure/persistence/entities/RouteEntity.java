package com.ub.higiea.infrastructure.persistence.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;

@Document("route")
public class RouteEntity {

    @Id
    private ObjectId id;

    @Field("truck_id")
    private Long truckId;

    @Field("sensor_ids")
    private List<Long> sensorIds;

    @Field("distance")
    private Double totalDistance;

    @Field("estimated_time")
    private Double estimatedTime;

    public RouteEntity() {
    }

    public RouteEntity(ObjectId id, Long truckId, List<Long> sensorIds, Double totalDistance, Double estimatedTime) {
        this.id = id;
        this.truckId = truckId;
        this.sensorIds = sensorIds;
        this.totalDistance = totalDistance;
        this.estimatedTime = estimatedTime;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
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