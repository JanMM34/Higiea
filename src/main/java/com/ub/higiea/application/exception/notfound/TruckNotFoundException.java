package com.ub.higiea.application.exception.notfound;

public class TruckNotFoundException extends RuntimeException {
    public TruckNotFoundException(Long id) {
        super("Truck with id " + id + " not found");
    }
}
