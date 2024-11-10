package com.ub.higiea.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("truck")
public class Truck {

    @Id
    @Column("id")
    private Long id;

    private Truck(){

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
