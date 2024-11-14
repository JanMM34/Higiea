package com.ub.higiea.domain.model;

public class Truck {

    private Long id;

    private Truck() {
    }

    private Truck(Long id) {
        this.id = id;
    }

    public static Truck create(Long id) {
        return new Truck(id);
    }

    public Long getId() {
        return id;
    }

}
