package com.ub.higiea.infrastructure.persistence.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJson;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "routes")
public class RouteEntity {

    @Id
    private String id;

    private Long truckId;

    private String type = "FeatureCollection";

    private List<Feature> features;

    public RouteEntity(String id, Long truckId, List<Feature> features) {
        this.id = id;
        this.features = features;
        this.truckId = truckId;
    }

    public String getId() {
        return this.id;
    }

    public Long getTruckId() {
        return this.truckId;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public static abstract class Feature {
        private final String type = "Feature";
        private final Map<String, Object> properties = new HashMap<>();
        private final GeoJson geometry;

        protected Feature(Map<String, Object> properties, GeoJson geometry) {
            if (properties != null) {
                this.properties.putAll(properties);
            }
            this.geometry = geometry;
        }

        public String getType() {
            return type;
        }

        public Map<String, Object> getProperties() {
            return properties;
        }

        public GeoJson getGeometry() {
            return geometry;
        }

    }

    public static class SensorFeature extends Feature {

        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        private final GeoJsonPoint location;

        public SensorFeature(Long id, String containerState, GeoJsonPoint location) {
            super(Map.of(
                    "id", id,
                    "containerState", containerState
            ), location);
            this.location = location;
        }

        public GeoJsonPoint getLocation() {
            return location;
        }

    }

    public static class RouteFeature extends Feature {

        @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
        private final GeoJsonLineString routeGeometry;

        public RouteFeature(Double totalDistance, Long estimatedTime, GeoJsonLineString routeGeometry) {
            super(Map.of(
                    "totalDistance", totalDistance,
                    "estimatedTime", estimatedTime
            ), routeGeometry);
            this.routeGeometry = routeGeometry;
        }

        public GeoJsonLineString getRouteGeometry() {
            return routeGeometry;
        }

    }

}