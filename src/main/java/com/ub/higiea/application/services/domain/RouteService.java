package com.ub.higiea.application.services.domain;

import com.ub.higiea.application.exception.notfound.NotFoundException;
import com.ub.higiea.application.exception.notfound.RouteNotFoundException;
import com.ub.higiea.application.requests.RouteCreateRequest;
import com.ub.higiea.application.utils.RouteCalculator;
import com.ub.higiea.application.dtos.RouteDTO;
import com.ub.higiea.application.exception.notfound.TruckNotFoundException;
import com.ub.higiea.domain.model.Route;
import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.model.Truck;
import com.ub.higiea.domain.repository.RouteRepository;
import com.ub.higiea.domain.repository.SensorRepository;
import com.ub.higiea.domain.repository.TruckRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;


@Service
public class RouteService {

    private final RouteCalculator routeCalculator;
    private final RouteRepository routeRepository;
    private final TruckRepository truckRepository;
    private final SensorRepository sensorRepository;

    public RouteService(RouteCalculator routeCalculator, RouteRepository routeRepository,
                        TruckRepository truckRepository, SensorRepository sensorRepository) {
        this.routeCalculator = routeCalculator;
        this.routeRepository = routeRepository;
        this.truckRepository = truckRepository;
        this.sensorRepository = sensorRepository;
    }

    public Flux<RouteDTO> getAllRoutes() {
        return routeRepository.findAll()
                .map(RouteDTO::fromRoute);
    }

    public Mono<RouteDTO> getRouteById(String routeId) {
        return routeRepository.findById(routeId)
                .switchIfEmpty(Mono.error(new RouteNotFoundException(routeId)))
                .map(RouteDTO::fromRoute);
    }

    public Mono<Void> deleteRouteById(String routeId) {
        return routeRepository.findById(routeId)
                .switchIfEmpty(Mono.error(new RouteNotFoundException(routeId)))
                .flatMap(route -> {
                    Truck truck = route.getTruck();
                    truck.unassignRoute();
                    return truckRepository.save(truck)
                            .then(routeRepository.deleteById(routeId));
                });
    }

    @Transactional
    public Mono<RouteDTO> createRoute(RouteCreateRequest request) {
        return fetchAvailableTruck()
                .switchIfEmpty(Mono.error(new NotFoundException("No available trucks found")))
                .zipWith(fetchSensors(request.getSensorIds())).flatMap(
                        tuple -> calculateAndSaveRoute(tuple.getT1(), tuple.getT2())
                ).map(RouteDTO::fromRoute);
    }

    private Mono<Truck> fetchAvailableTruck() {
        return truckRepository.findAll()
                .filter(truck -> !truck.hasAssignedRoute())
                .next();
    }

    private Mono<List<Sensor>> fetchSensors(List<Long> sensorIds) {
        return Flux.fromIterable(sensorIds)
                .flatMap(sensorRepository::findById)
                .collectList()
                .flatMap(sensors -> {
                    if (sensors.size() != sensorIds.size()) {
                        return Mono.error(new NotFoundException("One or more sensors not found."));
                    }
                    return Mono.just(sensors);
                });
    }

    private Mono<Route> calculateAndSaveRoute(Truck truck, List<Sensor> sensors) {
        return routeCalculator.calculateRoute(sensors)
                .flatMap(result -> {
                    Route route = Route.create(
                            null,
                            truck,
                            result.getOrderedSensors(),
                            result.getTotalDistance(),
                            result.getEstimatedTimeInSeconds(),
                            result.getRouteGeometry()
                    );

                    return routeRepository.save(route)
                            .flatMap(savedRoute -> {
                                truck.assignRoute(savedRoute);
                                return truckRepository.save(truck)
                                        .thenReturn(savedRoute);
                            })
                            .onErrorResume(e ->
                                    routeRepository.deleteById(route.getId())
                                    .then(Mono.error(e))
                            );
                });
    }

}