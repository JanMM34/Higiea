package com.ub.higiea.domain.model;

public class Location {

    double latitude;
    double longitude;

    private Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Location create(double latitude, double longitude) {
        return new Location(latitude, longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
