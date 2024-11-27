package com.ub.higiea.application.services;

import com.ub.higiea.application.services.domain.RouteService;
import com.ub.higiea.application.services.domain.SensorService;
import com.ub.higiea.application.services.domain.TruckService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
        return sensorService.updateSensorState(sensorId, state)
                .flatMap(sensor -> {
                   if(sensor.getContainerState().name().equals("FULL")) {
                       return handleFullContainer();
                   }
                   return Mono.empty();
                })
                .then();
    }

    private Mono<Void> handleFullContainer() {
        return truckService.fetchAvailableTruck()
                .flatMap(truck ->
                    sensorService.fetchRelevantSensors(truck.getMaxLoadCapacity())
                            .collectList()
                            .flatMap(sensors ->
                                    routeService.calculateAndSaveRoute(truck, sensors)
                                            .flatMap(savedRoute ->
                                                    truckService.assignRouteToTruck(truck, savedRoute)
                                            )
                            )
                )
                .then();
    }

}
