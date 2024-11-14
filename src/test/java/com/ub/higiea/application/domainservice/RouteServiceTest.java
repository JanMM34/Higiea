package com.ub.higiea.application.domainservice;

import com.ub.higiea.application.utils.RouteCalculator;
import com.ub.higiea.domain.model.*;
import com.ub.higiea.domain.repository.RouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class RouteServiceTest {
    private RouteService routeService;
    private RouteCalculator routeCalculator;
    private RouteRepository routeRepository;

    @BeforeEach
    public void setUp() {
        routeCalculator = Mockito.mock(RouteCalculator.class);
        routeRepository = Mockito.mock(RouteRepository.class);
        routeService = new RouteService(routeCalculator, routeRepository);
    }

    @Test
    void createRoute_shouldSaveRoute_WhenValidInput() {

        Truck truck = Truck.create(1L);

        Sensor sensor1 = Sensor.create(1L,Location.create(20.0,15.0), ContainerState.FULL);
        Sensor sensor2 = Sensor.create(2L, Location.create(30.0,15.0), ContainerState.FULL);

        List<Sensor> sensors = List.of(sensor1,sensor2);
        List<Sensor> orderedSensors = List.of(sensor1,sensor2);

        Mockito.when(routeCalculator.calculateRoute(sensors)).thenReturn(Mono.just(orderedSensors));
        Mockito.when(routeCalculator.calculateTotalDistance(orderedSensors)).thenReturn(Mono.just(30.0));
        Mockito.when(routeCalculator.calculateEstimatedTime(orderedSensors)).thenReturn(Mono.just(45.0));

        Route expectedRoute = Route.create("1",truck, List.of(sensor1, sensor2), 30.0, 45.0);
        Mockito.when(routeRepository.save(Mockito.any(Route.class))).thenReturn(Mono.just(expectedRoute));

        Mono<Route> result = routeService.createRoute(truck, sensors);
        StepVerifier.create(result)
                .expectNextMatches(route ->
                        route.getTruck().equals(truck) &&
                                route.getSensors().equals(List.of(sensor1, sensor2)) &&
                                route.getTotalDistance().equals(30.0) &&
                                route.getEstimatedTime().equals(45.0)
                )
                .verifyComplete();


        Mockito.verify(routeCalculator).calculateRoute(sensors);
        Mockito.verify(routeCalculator).calculateTotalDistance(orderedSensors);
        Mockito.verify(routeCalculator).calculateEstimatedTime(orderedSensors);
        Mockito.verify(routeRepository).save(Mockito.any(Route.class));
    }
}
