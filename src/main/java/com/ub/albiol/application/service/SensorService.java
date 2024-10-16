package com.ub.albiol.application.service;

import com.ub.albiol.application.SensorDTO.SensorDTO;
import com.ub.albiol.domain.model.Sensor;
import com.ub.albiol.domain.repository.SensorRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

}
