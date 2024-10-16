package com.ub.albiol.application.service;

import com.ub.albiol.application.SensorDTO.SensorDTO;
import com.ub.albiol.domain.model.Sensor;
import com.ub.albiol.domain.repository.SensorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
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
        Sensor sensor1 = new Sensor("Location1");
        Sensor sensor2 = new Sensor("Location2");
        Mockito.when(sensorRepository.findAll()).thenReturn(Flux.just(sensor1, sensor2));

        StepVerifier.create(sensorService.getAllSensors())
                .expectNext(SensorDTO.toObject(sensor1))
                .expectNext(SensorDTO.toObject(sensor2))
                .verifyComplete();
    }

    @Test
    void getAllSensors_shouldReturnEmptyFlux_whenRepositoryIsEmpty() {
        Mockito.when(sensorRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(sensorService.getAllSensors())
                .verifyComplete();
    }
}
