package com.ub.higiea.application.dtos;

import com.ub.higiea.domain.model.Truck;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class TruckDTO implements Serializable {


    private Long id;

    public TruckDTO(){

    }

    public TruckDTO(Long id) {
        this.id = id;
    }

    public static TruckDTO fromTruck(Truck truck) {
        return new TruckDTO(
                truck.getId()
        );
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruckDTO truckDTO = (TruckDTO) o;
        return Objects.equals(this.id,truckDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
