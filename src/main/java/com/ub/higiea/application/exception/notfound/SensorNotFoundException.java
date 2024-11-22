package com.ub.higiea.application.exception.notfound;

public class SensorNotFoundException extends NotFoundException {
    public SensorNotFoundException(Long id) {
        super("Sensor with id " + id + " not found");
    }
}
