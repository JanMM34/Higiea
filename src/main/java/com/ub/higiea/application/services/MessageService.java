package com.ub.higiea.application.services;

import com.ub.higiea.application.services.domain.RouteService;
import com.ub.higiea.application.utils.RoutePlanningStrategy;
import com.ub.higiea.application.utils.RouteTriggerStrategy;
import com.ub.higiea.application.services.domain.SensorService;
import com.ub.higiea.application.services.domain.TruckService;
import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.model.Truck;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final SensorService sensorService;
    private final TruckService truckService;
    private final RouteService routeService;
    private final RouteTriggerStrategy routeTriggerStrategy;
    private final RoutePlanningStrategy routePlanningStrategy;

    public MessageService(SensorService sensorService, TruckService truckService, RouteService routeService,
                          RouteTriggerStrategy routeTriggerStrategy, RoutePlanningStrategy routePlanningStrategy) {

        this.sensorService = sensorService;
        this.truckService = truckService;
        this.routeService = routeService;
        this.routeTriggerStrategy = routeTriggerStrategy;
        this.routePlanningStrategy = routePlanningStrategy;
    }

    public Mono<Void> handleMessage(UUID sensorId, int state) {
        return sensorService.updateSensorState(sensorId, state)
                .flatMap(sensor -> routeTriggerStrategy.shouldTriggerRoute(sensor)
                        .flatMap(shouldTrigger -> {
                            if (shouldTrigger) {
                                return triggerRoute();
                            } else {
                                return Mono.empty();
                            }
                        })
                ).then();
    }

    private Mono<Void> triggerRoute() {
        return routePlanningStrategy.prepareRoute()
                .flatMap(tuple -> {
                    Truck truck = tuple.getT1();
                    List<Sensor> sensors = tuple.getT2();
                    return routeService.calculateAndSaveRoute(truck, sensors)
                            .flatMap(route -> {
                                sensors.forEach(Sensor::markAssignedToRoute);
                                return Mono.when(
                                        sensorService.saveAll(sensors),
                                        truckService.assignRouteToTruck(truck,route)
                                );
                            });
                })
                .then();
    }

}
