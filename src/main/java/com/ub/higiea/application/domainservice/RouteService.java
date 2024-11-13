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


@Service
public class RouteService {

    private final RouteCalculator routeCalculator;
    private final RouteRepository routeRepository;

    public RouteService(RouteCalculator routeCalculator, RouteRepository routeRepository) {
        this.routeCalculator = routeCalculator;
        this.routeRepository = routeRepository;
    }

    public Mono<Route> createRoute(Truck truck, List<Sensor> sensors) {
        List<Sensor> orderedSensors = routeCalculator.calculateRoute(sensors);
        List<Long> sensorIds = orderedSensors.stream().map(Sensor::getId).toList();

        Double totalDistance = routeCalculator.calculateTotalDistance(orderedSensors);
        Double estimatedTime = routeCalculator.calculateEstiamtedTime(orderedSensors);

        Route route = Route.create(truck.getId(),sensorIds,totalDistance,estimatedTime);
        return routeRepository.save(route);
    }
}
