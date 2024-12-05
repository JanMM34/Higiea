package com.ub.higiea.application.services.domain;

import com.ub.higiea.application.dtos.TruckDTO;
import com.ub.higiea.application.exception.notfound.NotFoundException;
import com.ub.higiea.application.exception.notfound.TruckNotFoundException;
import com.ub.higiea.application.requests.SensorCreateRequest;
import com.ub.higiea.application.requests.TruckCreateRequest;
import com.ub.higiea.domain.model.Location;
import com.ub.higiea.domain.model.Route;
import com.ub.higiea.domain.model.Truck;
import com.ub.higiea.domain.repository.TruckRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TruckService {
    private final TruckRepository truckRepository;
    public TruckService(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
    }

    public Flux<TruckDTO> getAllTrucks() {
        return truckRepository.findAll()
                .map(TruckDTO::fromTruck);
    }

    public Mono<TruckDTO> getTruckById(Long id) {
        return truckRepository.findById(id)
                .switchIfEmpty(Mono.error(new TruckNotFoundException(id)))
                .map(TruckDTO::fromTruck);
    }

    public Mono<TruckDTO> createTruck(TruckCreateRequest request) {
        return truckRepository.save(
                Truck.create(null, request.getMaxLoadCapacity(),
                        Location.create(request.getLatitude(), request.getLongitude()))
                )
                .map(TruckDTO::fromTruck);
    }

    public Mono<Truck> fetchAvailableTruck() {
        return truckRepository.findAll()
                .filter(truck -> !truck.hasAssignedRoute())
                .next();
    }

    public Mono<TruckDTO> unassignRouteFromTruck(Long truckId) {
        return truckRepository.findById(truckId)
                .switchIfEmpty(Mono.error(new TruckNotFoundException(truckId)))
                .flatMap(truck -> {
                    if (truck.hasAssignedRoute()) {
                        truck.unassignRoute();
                        return truckRepository.save(truck).map(TruckDTO::fromTruck);
                    } else {
                        return Mono.error(new IllegalArgumentException("Truck does not have an assigned route."));
                    }
                });
    }


    public Mono<Truck> assignRouteToTruck(Truck truck, Route savedRoute) {
        truck.assignRoute(savedRoute);
        return truckRepository.save(truck);
    }

    public Mono<Truck> getTruckByIdAsEntity(Long truckId) {
        return truckRepository.findById(truckId).switchIfEmpty(Mono.error(new TruckNotFoundException(truckId)));
    }

}
