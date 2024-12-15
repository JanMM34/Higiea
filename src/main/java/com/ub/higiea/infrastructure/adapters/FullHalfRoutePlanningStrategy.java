package com.ub.higiea.infrastructure.adapters;

import com.ub.higiea.application.services.domain.SensorService;
import com.ub.higiea.application.services.domain.TruckService;
import com.ub.higiea.application.utils.RoutePlanningStrategy;
import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.model.Truck;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;

@Component
public class FullHalfRoutePlanningStrategy implements RoutePlanningStrategy {

    private final SensorService sensorService;
    private final TruckService truckService;

    public FullHalfRoutePlanningStrategy(SensorService sensorService, TruckService truckService) {
        this.sensorService = sensorService;
        this.truckService = truckService;
    }

    @Override
    public Mono<Tuple2<Truck, List<Sensor>>> prepareRoute() {
        return sensorService.fetchSensorsByPriorityState()
                .collectList()
                .flatMap(sensors -> {
                    int totalCapacity = sensors.stream()
                            .mapToInt(sensor -> sensor.getContainerState().getLevel())
                            .sum();

                    return truckService.fetchOptimalTruck(totalCapacity)
                            .map(truck -> Tuples.of(truck, sensors));
                });
    }
}
