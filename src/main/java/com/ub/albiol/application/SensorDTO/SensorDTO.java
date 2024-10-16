package com.ub.albiol.application.SensorDTO;

import com.ub.albiol.domain.model.Sensor;
import lombok.Data;

import java.io.Serializable;

@Data
public class SensorDTO implements Serializable {

    private String location;

    public SensorDTO() {}

    private SensorDTO(String location){
        this.location = location;
    }


    public static SensorDTO toObject(String location) {
        return new SensorDTO(location);
    }

    public static SensorDTO toObject(Sensor sensor) {
        return new SensorDTO(sensor.getLocation());
    }
}
