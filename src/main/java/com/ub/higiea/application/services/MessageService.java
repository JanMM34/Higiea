package com.ub.higiea.application.services;

import com.ub.higiea.application.services.domain.RouteService;
import com.ub.higiea.application.ports.RoutePlanning;
import com.ub.higiea.application.ports.RouteTrigger;
import com.ub.higiea.application.services.domain.SensorService;
import com.ub.higiea.application.services.domain.TruckService;
import com.ub.higiea.domain.model.ContainerState;
import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.model.Truck;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class MessageService {

    private final SensorService sensorService;
    private final TruckService truckService;
    private final RouteService routeService;
    private final RouteTrigger routeTrigger;
    private final RoutePlanning routePlanning;

    public MessageService(SensorService sensorService, TruckService truckService, RouteService routeService,
                          RouteTrigger routeTrigger, RoutePlanning routePlanning) {

        this.sensorService = sensorService;
        this.truckService = truckService;
        this.routeService = routeService;
        this.routeTrigger = routeTrigger;
        this.routePlanning = routePlanning;
    }

    public Mono<Void> handleMessage(UUID sensorId, int state) {
        log.info("Handling message for sensor ID: {}, state: {}", sensorId, state);
        return sensorService.updateSensorState(sensorId, state)
                .flatMap(sensor -> {
                    if (sensor.getContainerState() == ContainerState.EMPTY) {
                        return handleEmptySensor(sensor);
                    } else {
                        return routeTrigger.shouldTriggerRoute(sensor)
                                .flatMap(shouldTrigger -> {
                                    if (shouldTrigger) {
                                        log.info("Route trigger condition met. Triggering route.");
                                        return triggerRoute();
                                    }
                                    return Mono.empty();
                                });
                    }
                })
                .doOnError(error -> log.error("Error handling message for sensor ID: {}", sensorId, error))
                .then();
    }

    private Mono<Void> handleEmptySensor(Sensor sensor) {
        return routeService.checkIfLastSensor(sensor)
                .flatMap(isLast -> {
                    if (isLast) {
                        log.debug("Sensor {} is the last sensor. Handling last sensor.", sensor);
                        return handleLastSensor(sensor);
                    }
                    return Mono.empty();
                })
                .then(sensorService.markSensorUnassigned(sensor))
                .doOnSuccess(unused -> log.debug("Successfully marked sensor as unassigned: {}", sensor))
                .doOnError(error -> log.error("Error marking sensor as unassigned: {}", sensor, error));
    }

    private Mono<Void> handleLastSensor(Sensor sensor) {
        return routeService.getRouteEntityById(sensor.getAssignedRoute().getId())
                .flatMap(route -> truckService.unassignRouteFromTruck(route.getTruck().getId())).then();
    }

    private Mono<Void> triggerRoute() {
        return routePlanning.prepareRoute()
                .flatMap(tuple -> {

                    Truck truck = tuple.getT1();
                    List<Sensor> sensors = tuple.getT2();
                    log.info("Preparing route with truck ID: {}", truck.getId());
                    log.info("Sensors assigned to the route: {}", sensors.stream()
                            .map(sensor -> String.format("Sensor ID: %s, Location: (%f, %f), State: %s",
                                    sensor.getId(),
                                    sensor.getLocation().getLatitude(),
                                    sensor.getLocation().getLongitude(),
                                    sensor.getContainerState()))
                            .toList());

                    return routeService.calculateAndSaveRoute(truck, sensors)
                            .flatMap(route -> Mono.when(
                                    sensorService.assignRouteToSensors(sensors,route),
                                    truckService.assignRouteToTruck(truck,route)
                            ));
                })
                .doOnSuccess(unused -> log.info("Route preparation completed successfully."))
                .doOnError(error -> log.error("Error occurred during route preparation.", error))
                .then();
    }

}
