package com.ub.albiol.infrastructure.controller;

import com.ub.albiol.application.SensorDTO.SensorDTO;
import com.ub.albiol.application.service.SensorService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("sensors")
public class SensorController {

    private final SensorService sensorService;


    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }


    @GetMapping(produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<SensorDTO> getAllSensors() {
        return sensorService.getAllSensors();
    }
}
