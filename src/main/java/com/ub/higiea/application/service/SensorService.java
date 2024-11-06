package com.ub.higiea.application.service;

import com.ub.higiea.application.DTOs.SensorDTO;
import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.repository.SensorRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class SensorService {
    private final SensorRepository sensorRepository;
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public Flux<SensorDTO> getAllSensors() {
        return sensorRepository.findAll()
                .map(SensorDTO::toObject);
    }

    public Mono<SensorDTO> getSensorById(Long id) {
        return sensorRepository.findById(id)
                .map(SensorDTO::toObject);
    }

    public Mono<SensorDTO> createSensor(SensorDTO sensorDTO) {
        Sensor sensor = Sensor.create(
                sensorDTO.getId(),
                sensorDTO.getLocation().getY(), // latitude
                sensorDTO.getLocation().getX(), // longitude
                sensorDTO.getContainerState()
        );
        return sensorRepository.save(sensor)
                .map(SensorDTO::toObject);
    }

}
