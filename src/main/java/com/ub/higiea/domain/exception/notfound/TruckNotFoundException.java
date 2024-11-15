package com.ub.higiea.domain.exception.notfound;

public class TruckNotFoundException extends NotFoundException {
    public TruckNotFoundException(Long id) {
        super("Truck with id " + id + " not found");
    }
}
