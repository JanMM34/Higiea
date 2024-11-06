package com.ub.higiea.infrastructure.controller;

import com.ub.higiea.application.DTOs.SensorDTO;
import com.ub.higiea.application.service.SensorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("sensors")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<SensorDTO> getAllSensors() {
        return sensorService.getAllSensors();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<SensorDTO> getSensor(@PathVariable("id") Long sensorId) {
        return sensorService.getSensorById(sensorId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found")));

    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<SensorDTO> createSensor(@RequestBody SensorDTO sensorDTO) {
        return sensorService.createSensor(sensorDTO);
    }

}
