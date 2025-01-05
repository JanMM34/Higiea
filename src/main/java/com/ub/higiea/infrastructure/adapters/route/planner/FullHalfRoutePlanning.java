package com.ub.higiea.infrastructure.adapters.route.planner;

import com.ub.higiea.application.services.domain.SensorService;
import com.ub.higiea.application.services.domain.TruckService;
import com.ub.higiea.application.ports.RoutePlanning;
import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.model.Truck;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.ArrayList;
import java.util.List;

@Component
public class FullHalfRoutePlanning implements RoutePlanning {

    private final SensorService sensorService;
    private final TruckService truckService;

    public FullHalfRoutePlanning(SensorService sensorService, TruckService truckService) {
        this.sensorService = sensorService;
        this.truckService = truckService;
    }

    @Override
    public Mono<Tuple2<Truck, List<Sensor>>> prepareRoute() {
        return sensorService.fetchSensorsByPriorityState()
                .collectList()
                .flatMap(this::assignTruck)
                .switchIfEmpty(Mono.empty());
    }

    private Mono<Tuple2<Truck, List<Sensor>>> assignTruck(List<Sensor> sensors) {
        if (sensors.isEmpty()) {
            return Mono.empty();
        }

        int totalCapacity = calculateTotalCapacity(sensors);
        return truckService.fetchOptimalTruck(totalCapacity)
                .flatMap(truck -> Mono.just(Tuples.of(truck, sensors)))
                .switchIfEmpty(Mono.defer(() -> attemptWithBiggestTruck(sensors)));
    }

    private Mono<Tuple2<Truck, List<Sensor>>> attemptWithBiggestTruck(List<Sensor> sensors) {
        return truckService.fetchBiggestTruck()
                .flatMap(biggestTruck -> {
                    List<Sensor> adjustedSensors = new ArrayList<>(sensors);
                    int truckCapacity = biggestTruck.getMaxLoadCapacity();

                    while (!adjustedSensors.isEmpty() && calculateTotalCapacity(adjustedSensors) > truckCapacity) {
                        adjustedSensors.removeLast();
                    }

                    if (adjustedSensors.isEmpty()) {
                        return Mono.empty();
                    }

                    return Mono.just(Tuples.of(biggestTruck, adjustedSensors));
                })
                .switchIfEmpty(Mono.empty());
    }


    private int calculateTotalCapacity(List<Sensor> sensors) {
        return sensors.stream()
                .mapToInt(s -> s.getContainerState().getLevel())
                .sum();
    }

}