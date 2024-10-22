package com.ub.higiea.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table("sensor")
@Data
@NoArgsConstructor
public class Sensor {

    @Id
    private Long id;
    private String location;

    public Sensor(String location) {
        this.location = location;
    }
}
