package com.ub.higiea.infrastructure.controller;

import com.ub.higiea.application.SensorDTO.SensorDTO;
import com.ub.higiea.application.service.SensorService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping(params = "id", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<SensorDTO> getSensor(@RequestParam("id") String sensorId) {
        // Returning an empty Flux for testing purposes
        return Flux.empty();
    }
}
