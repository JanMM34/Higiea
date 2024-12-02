package com.ub.higiea.application.services;

import com.ub.higiea.application.services.domain.RouteService;
import com.ub.higiea.application.services.domain.SensorService;
import com.ub.higiea.application.services.domain.TruckService;
import com.ub.higiea.application.utils.RouteCalculationResult;
import com.ub.higiea.application.utils.RouteCalculator;
import com.ub.higiea.domain.model.*;
import com.ub.higiea.domain.repository.RouteRepository;
import com.ub.higiea.domain.repository.SensorRepository;
import com.ub.higiea.domain.repository.TruckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MessageServiceIntegrationTest {

    @Mock
    private SensorRepository sensorRepository;

    @Mock
    private TruckRepository truckRepository;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private RouteCalculator routeCalculator;

    private SensorService sensorService;
    private TruckService truckService;
    private RouteService routeService;


    private MessageService messageService;

    @BeforeEach
    void setUp() {

        sensorService = new SensorService(sensorRepository);
        truckService = new TruckService(truckRepository);
        routeService = new RouteService(routeCalculator, routeRepository);

        messageService = new MessageService(sensorService, routeService, truckService);
    }

    @Test
    void handleMessage_FullFlow_Success() {
        Long sensorId = 1L;
        int stateFull = 80;

        Sensor existingSensor = Sensor.create(sensorId, Location.create(10.0, 20.0), ContainerState.EMPTY);

        Sensor updatedSensor = Sensor.create(sensorId, Location.create(10.0, 20.0), ContainerState.FULL);

        when(sensorRepository.findById(sensorId))
                .thenReturn(Mono.just(existingSensor))
                .thenReturn(Mono.just(updatedSensor));

        when(sensorRepository.save(any(Sensor.class))).thenReturn(Mono.just(updatedSensor));

        Sensor sensor2 = Sensor.create(2L, Location.create(15.0, 25.0), ContainerState.HALF);
        when(sensorRepository.findAll()).thenReturn(Flux.just(updatedSensor, sensor2));

        Truck truck = Truck.create(1L, 5, Location.create(30.0, 40.0));
        when(truckRepository.findAll()).thenReturn(Flux.just(truck));
        when(truckRepository.save(any(Truck.class))).thenReturn(Mono.just(truck));

        List<Sensor> sensors = List.of(updatedSensor, sensor2);
        RouteCalculationResult calculationResult = new RouteCalculationResult(
                sensors,
                100.0,
                200L,
                List.of(
                        Location.create(30.0, 40.0),
                        updatedSensor.getLocation(),
                        sensor2.getLocation(),
                        Location.create(30.0, 40.0)
                )
        );
        when(routeCalculator.calculateRoute(truck.getDepotLocation(), sensors))
                .thenReturn(Mono.just(calculationResult));

        Route route = Route.create("route1", truck, sensors, calculationResult.getTotalDistance(),
                calculationResult.getEstimatedTimeInSeconds(), calculationResult.getRouteGeometry());
        when(routeRepository.save(any(Route.class))).thenReturn(Mono.just(route));


        Mono<Void> result = messageService.handleMessage(sensorId, stateFull);


        StepVerifier.create(result)
                .verifyComplete();


        verify(sensorRepository, times(1)).findById(sensorId);
        verify(sensorRepository, times(1)).save(any(Sensor.class));
        verify(truckRepository, times(1)).findAll();
        verify(sensorRepository, times(1)).findAll();
        verify(routeCalculator, times(1)).calculateRoute(truck.getDepotLocation(), sensors);
        verify(routeRepository, times(1)).save(any(Route.class));
        verify(truckRepository, times(1)).save(any(Truck.class));

    }

    @Test
    void handleMessage_NoTruckAvailable_ShouldCompleteWithoutErrors() {

        Long sensorId = 1L;
        int state = 80;

        Sensor existingSensor = Sensor.create(sensorId, Location.create(10.0, 20.0), ContainerState.EMPTY);
        Sensor updatedSensor = Sensor.create(sensorId, Location.create(10.0, 20.0), ContainerState.FULL);

        when(sensorRepository.findById(sensorId))
                .thenReturn(Mono.just(existingSensor))
                .thenReturn(Mono.just(updatedSensor));

        when(sensorRepository.save(any(Sensor.class))).thenReturn(Mono.just(updatedSensor));


        when(truckRepository.findAll()).thenReturn(Flux.empty());


        Mono<Void> result = messageService.handleMessage(sensorId, state);


        StepVerifier.create(result)
                .verifyComplete();


        verify(sensorRepository, times(1)).findById(sensorId);
        verify(sensorRepository, times(1)).save(any(Sensor.class));
        verify(truckRepository, times(1)).findAll();
        verifyNoMoreInteractions(sensorRepository, truckRepository, routeRepository, routeCalculator);

    }

}
