package com.ub.higiea.application.services;

import com.ub.higiea.application.dtos.RouteDTO;
import com.ub.higiea.application.services.domain.RouteService;
import com.ub.higiea.application.services.domain.SensorService;
import com.ub.higiea.application.services.domain.TruckService;
import com.ub.higiea.domain.model.Route;
import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.model.Truck;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final SensorService sensorService;
    private final RouteService routeService;
    private final TruckService truckService;

    public MessageService(SensorService sensorService, RouteService routeService, TruckService truckService) {
        this.sensorService = sensorService;
        this.routeService = routeService;
        this.truckService = truckService;
    }

    public Mono<Void> handleMessage(UUID sensorId, int state) {
        return Mono.defer(() ->
                sensorService.updateSensorState(sensorId, state)
                        .flatMap(sensor -> {
                            if (sensor.getContainerState().name().equals("FULL")) {
                                return handleFullContainer();
                            }
                            return Mono.empty();
                        })
        ).then();
    }

    private Mono<Void> handleFullContainer() {
        return sensorService.fetchRelevantSensorsForRouting()
                .collectList()
                .flatMap(sensors -> {

                    int totalCapacity = sensors.stream()
                            .mapToInt(sensor -> sensor.getContainerState().getLevel())
                            .sum();

                    return truckService.fetchOptimalTruck(totalCapacity)
                            .flatMap(truck -> routeService.calculateAndSaveRoute(truck, sensors)
                                    .flatMap(route -> Mono.when(
                                            sensorService.saveAll(sensors),
                                            truckService.assignRouteToTruck(truck, route)
                                    ))
                            );
                })
                .then();
    }

}
