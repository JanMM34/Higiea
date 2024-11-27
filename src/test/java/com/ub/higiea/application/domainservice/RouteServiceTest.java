package com.ub.higiea.application.domainservice;

import com.ub.higiea.application.dtos.RouteDTO;
import com.ub.higiea.application.requests.RouteCreateRequest;
import com.ub.higiea.application.services.domain.RouteService;
import com.ub.higiea.application.utils.RouteCalculator;
import com.ub.higiea.application.utils.RouteCalculationResult;
import com.ub.higiea.domain.model.*;
import com.ub.higiea.domain.repository.RouteRepository;
import com.ub.higiea.domain.repository.SensorRepository;
import com.ub.higiea.domain.repository.TruckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class RouteServiceTest {

    private RouteService routeService;
    private RouteCalculator routeCalculator;
    private RouteRepository routeRepository;
    private TruckRepository truckRepository;
    private SensorRepository sensorRepository;

    @BeforeEach
    public void setUp() {
        routeCalculator = Mockito.mock(RouteCalculator.class);
        routeRepository = Mockito.mock(RouteRepository.class);
        truckRepository = Mockito.mock(TruckRepository.class);
        sensorRepository = Mockito.mock(SensorRepository.class);
        routeService = new RouteService(routeCalculator, routeRepository);
    }

    @Test
    void createRoute_shouldReturnRouteDTO_WhenValidInput() {

        Long truckId = 1L;
        List<Long> sensorIds = List.of(1L, 2L);

        RouteCreateRequest request = RouteCreateRequest.toRequest(sensorIds);

        Truck truck = Truck.create(truckId);
        Sensor sensor1 = Sensor.create(1L, Location.create(20.0, 10.0), ContainerState.EMPTY);
        Sensor sensor2 = Sensor.create(2L, Location.create(30.0, 20.0), ContainerState.FULL);
        List<Sensor> sensors = List.of(sensor1, sensor2);
        List<Sensor> orderedSensors = List.of(sensor1, sensor2);
        List<Location> routeGeometry = List.of(Location.create(20.0, 10.0), Location.create(30.0, 20.0));

        RouteCalculationResult calculationResult = new RouteCalculationResult(
                orderedSensors,
                30.0,
                45L,
                routeGeometry
        );

        Mockito.when(sensorRepository.findById(1L)).thenReturn(Mono.just(sensor1));
        Mockito.when(sensorRepository.findById(2L)).thenReturn(Mono.just(sensor2));
        Mockito.when(routeCalculator.calculateRoute(sensors)).thenReturn(Mono.just(calculationResult));

        Route route = Route.create(null, truck, orderedSensors, 30.0, 45L, routeGeometry);
        RouteDTO expectedRouteDTO = RouteDTO.fromRoute(route);

        Mockito.when(routeRepository.save(Mockito.any(Route.class))).thenReturn(Mono.just(route));
        Mockito.when(truckRepository.findAll()).thenReturn(Flux.just(truck));
        Mockito.when(truckRepository.save(Mockito.any(Truck.class))).thenReturn(Mono.just(truck));

        StepVerifier.create(routeService.createRoute(request))
                .expectNextMatches(dto -> dto.equals(expectedRouteDTO))
                .verifyComplete();

        Mockito.verify(sensorRepository).findById(1L);
        Mockito.verify(sensorRepository).findById(2L);
        Mockito.verify(routeCalculator).calculateRoute(sensors);
        Mockito.verify(routeRepository).save(Mockito.any(Route.class));

    }

}
