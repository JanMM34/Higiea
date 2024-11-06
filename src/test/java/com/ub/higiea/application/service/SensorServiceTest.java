package com.ub.higiea.application.service;

import com.ub.higiea.application.DTOs.SensorDTO;
import com.ub.higiea.domain.model.ContainerState;
import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.repository.SensorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.geo.Point;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class SensorServiceTest {

    private SensorService sensorService;

    @MockBean
    private SensorRepository sensorRepository;

    @BeforeEach
    public void setUp() {
        sensorRepository = Mockito.mock(SensorRepository.class);
        sensorService = new SensorService(sensorRepository);
    }

    @Test
    void getAllSensors_ShouldReturnAllSensorsDTOs_WhenRepositoryNotEmpty() {
        Sensor sensor1 = Sensor.create(1L, 10.0, 20.0, ContainerState.EMPTY);
        Sensor sensor2 = Sensor.create(2L, 15.0, 25.0, ContainerState.FULL);

        SensorDTO sensor1DTO = SensorDTO.toObject(sensor1);
        SensorDTO sensor2DTO = SensorDTO.toObject(sensor2);

        Mockito.when(sensorRepository.findAll()).thenReturn(Flux.just(sensor1, sensor2));

        StepVerifier.create(sensorService.getAllSensors())
                .expectNext(sensor1DTO)
                .expectNext(sensor2DTO)
                .verifyComplete();
    }

    @Test
    void getAllSensors_shouldReturnEmptyFlux_whenRepositoryIsEmpty() {
        Mockito.when(sensorRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(sensorService.getAllSensors())
                .verifyComplete();
    }
}
