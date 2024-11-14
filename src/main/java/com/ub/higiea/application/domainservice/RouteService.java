package com.ub.higiea.application.domainservice;

import com.ub.higiea.application.utils.RouteCalculator;
import com.ub.higiea.domain.model.Route;
import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.model.Truck;
import com.ub.higiea.domain.repository.RouteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class RouteService {

    private final RouteCalculator routeCalculator;
    private final RouteRepository routeRepository;

    public RouteService(RouteCalculator routeCalculator, RouteRepository routeRepository) {
        this.routeCalculator = routeCalculator;
        this.routeRepository = routeRepository;
    }

    public Mono<Route> createRoute(Truck truck, List<Sensor> sensors) {
        return routeCalculator.calculateRoute(sensors)
                .flatMap(orderedSensors -> {
                    Mono<Double> totalDistanceMono = routeCalculator.calculateTotalDistance(orderedSensors);
                    Mono<Double> estimatedTimeMono = routeCalculator.calculateEstimatedTime(orderedSensors);

                    return Mono.zip(totalDistanceMono, estimatedTimeMono)
                            .flatMap(tuple -> {
                                Double totalDistance = tuple.getT1();
                                Double estimatedTime = tuple.getT2();

                                Route route = Route.create(null, truck, orderedSensors, totalDistance, estimatedTime);
                                return routeRepository.save(route);
                            });
                });
    }
}
