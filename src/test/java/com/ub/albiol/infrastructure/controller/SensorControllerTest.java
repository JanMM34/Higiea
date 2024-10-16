package com.ub.albiol.infrastructure.controller;

import com.ub.albiol.application.SensorDTO.SensorDTO;
import com.ub.albiol.application.service.SensorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

@WebFluxTest(controllers = SensorController.class)
public class SensorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SensorService sensorService;

    @Test
    void getAllSensors() {
        SensorDTO sensor1 = SensorDTO.toObject("Location1");
        SensorDTO sensor2 = SensorDTO.toObject("Location2");

        Mockito.when(sensorService.getAllSensors()).thenReturn(Flux.just(sensor1, sensor2));

        webTestClient.get().uri("/sensors")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(SensorDTO.class)
                .hasSize(1)
                .contains(sensor1, sensor2);
    }

}
