package com.ub.higiea.application.services;

import com.ub.higiea.application.dtos.RouteDTO;
import com.ub.higiea.application.requests.RouteCreateRequest;
import com.ub.higiea.application.services.domain.RouteService;
import com.ub.higiea.application.services.domain.SensorService;
import com.ub.higiea.application.services.domain.TruckService;
import com.ub.higiea.domain.model.Route;
import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.model.Truck;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    public Mono<Void> handleMessage(Long sensorId, int state) {
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
        return truckService.fetchAvailableTruck()
                .flatMap(truck -> sensorService.fetchRelevantSensors(truck.getMaxLoadCapacity())
                        .collectList()
                        .flatMap(sensors -> Mono.zip(
                                Mono.just(truck),
                                routeService.calculateAndSaveRoute(truck, sensors)
                        ).flatMap(tuple -> {
                            Truck assignedTruck = tuple.getT1();
                            Route route = tuple.getT2();

                            return Mono.when(
                                    sensorService.saveAll(sensors),
                                    truckService.assignRouteToTruck(assignedTruck, route)
                            );
                        }))
                ).then();
    }

    public Mono<RouteDTO> createRoute(RouteCreateRequest request) {
        Mono<Truck> truckMono = truckService.getTruckByIdAsEntity(request.getTruckId());
        Flux<Sensor> sensorsFlux = sensorService.getSensorsByIds(request.getSensorIds());

        return Mono.zip(truckMono, sensorsFlux.collectList())
                .flatMap(tuple -> {
                    Truck truck = tuple.getT1();
                    List<Sensor> sensors = tuple.getT2();
                    return routeService.calculateAndSaveRoute(truck, sensors)
                            .flatMap(route -> truckService.assignRouteToTruck(truck, route)
                                    .thenReturn(RouteDTO.fromRoute(route)));
                });
    }

}
