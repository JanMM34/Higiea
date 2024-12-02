package com.ub.higiea.application.requests;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class TruckCreateRequest {

    @NotNull(message = "Max load capacity cannot be null")
    @Min(value = 1, message = "Max load capacity must be greater than 0")
    private Integer maxLoadCapacity;

    @NotNull(message = "Latitude cannot be null")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private Double latitude;

    @NotNull(message = "Longitude cannot be null")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private Double longitude;

    public TruckCreateRequest(Double latitude, Double longitude, Integer maxLoadCapacity) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.maxLoadCapacity = maxLoadCapacity;
    }

    public static TruckCreateRequest toRequest(Double latitude, Double longitude, Integer maxLoadCapacity) {
        return new TruckCreateRequest(latitude,longitude, maxLoadCapacity);
    }

    public Integer getMaxLoadCapacity() {
        return this.maxLoadCapacity;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

}
