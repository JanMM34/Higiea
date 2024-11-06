package com.ub.higiea.infrastructure.controller;

import com.ub.higiea.application.DTOs.SensorDTO;
import com.ub.higiea.application.service.SensorService;
import com.ub.higiea.domain.model.ContainerState;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = SensorController.class)
public class SensorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private SensorService sensorService;

    @Test
    void getAllSensors_ShouldReturnListOfSensorDTOs() {

        SensorDTO sensorDTO1 = SensorDTO.fromData(1L, 40.7128, -74.0060, ContainerState.FULL);
        SensorDTO sensorDTO2 = SensorDTO.fromData(2L, 34.0522, -118.2437, ContainerState.EMPTY);

        Mockito.when(sensorService.getAllSensors()).thenReturn(Flux.just(sensorDTO1, sensorDTO2));

        webTestClient.get().uri("/sensors")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(SensorDTO.class)
                .hasSize(2)
                .contains(sensorDTO1, sensorDTO2);
    }

    @Test
    void getSensorById_ShouldReturnSensorDTO_WhenSensorExists() {
        Long sensorId = 1L;
        SensorDTO sensorDTO = SensorDTO.fromData(sensorId, 40.7128, -74.0060, ContainerState.FULL);

        Mockito.when(sensorService.getSensorById(sensorId)).thenReturn(Mono.just(sensorDTO));

        webTestClient.get()
                .uri("/sensors/{id}", sensorId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(SensorDTO.class)
                .isEqualTo(sensorDTO);
    }

    @Test
    void getSensorById_ShouldReturnNotFound_WhenSensorDoesNotExist() {
        Long sensorId = 1L;

        Mockito.when(sensorService.getSensorById(sensorId)).thenReturn(Mono.empty());

        webTestClient.get()
                .uri("/sensors/{id}", sensorId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void createSensor_ShouldReturnCreatedSensorDTO() {

        SensorDTO sensorDTO = SensorDTO.fromData(3L, 37.7749, -122.4194, ContainerState.EMPTY);

        Mockito.when(sensorService.createSensor(sensorDTO)).thenReturn(Mono.just(sensorDTO));

        webTestClient.post()
                .uri("/sensors")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(sensorDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(SensorDTO.class)
                .isEqualTo(sensorDTO);
    }
}
