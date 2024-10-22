package com.ub.higiea.application.service;

import com.ub.higiea.application.SensorDTO.SensorDTO;
import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.repository.SensorRepository;
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
