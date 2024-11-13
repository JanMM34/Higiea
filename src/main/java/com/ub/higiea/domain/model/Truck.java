package com.ub.higiea.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("truck")
public class Truck {

    @Id
    @Column("id")
    private Long id;

    private Truck() {
    }

    public static Truck create() {
        return new Truck();
    }

    public Long getId() {
        return id;
    }

}
