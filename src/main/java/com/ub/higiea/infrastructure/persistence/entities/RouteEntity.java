package com.ub.higiea.infrastructure.persistence.entities;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Map;

@Document("route")
public class RouteEntity {

    @Id
    private ObjectId id;

    private String type; // "FeatureCollection"
    private List<Map<String, Object>> features; // Each element is a GeoJSON feature

    public RouteEntity() {
    }

    public RouteEntity(ObjectId id, String type, List<Map<String, Object>> features) {
        this.id = id;
        this.type = type;
        this.features = features;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Map<String, Object>> getFeatures() {
        return features;
    }

    public void setFeatures(List<Map<String, Object>> features) {
        this.features = features;
    }
}
