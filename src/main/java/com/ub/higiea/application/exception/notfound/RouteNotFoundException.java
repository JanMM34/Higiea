package com.ub.higiea.application.exception.notfound;

public class RouteNotFoundException extends NotFoundException {
    public RouteNotFoundException(String id) {
        super("Route with id " + id + " not found");
    }
}
