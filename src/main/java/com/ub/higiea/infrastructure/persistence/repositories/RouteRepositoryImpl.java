package com.ub.higiea.infrastructure.persistence.repositories;

import com.ub.higiea.domain.model.Route;
import com.ub.higiea.domain.model.Truck;
import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.repository.RouteRepository;
import com.ub.higiea.domain.repository.SensorRepository;
import com.ub.higiea.domain.repository.TruckRepository;
import com.ub.higiea.infrastructure.persistence.entities.RouteEntity;
import com.ub.higiea.infrastructure.persistence.mapper.RouteMapper;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class RouteRepositoryImpl implements RouteRepository {

    private final RouteEntityRepository routeEntityRepository;
    private final TruckRepository truckRepository;
    private final SensorRepository sensorRepository;

    public RouteRepositoryImpl(RouteEntityRepository routeEntityRepository,
                               TruckRepository truckRepository,
                               SensorRepository sensorRepository) {

        this.routeEntityRepository = routeEntityRepository;
        this.truckRepository = truckRepository;
        this.sensorRepository = sensorRepository;
    }

    @Override
    public Mono<Route> findById(String id) {
        return routeEntityRepository.findById(new ObjectId(id))
                .flatMap(entity ->
                        Mono.zip(
                                truckRepository.findById(entity.getTruckId()),
                                Flux.fromIterable(entity.getSensorIds())
                                        .flatMap(sensorRepository::findById)
                                        .collectList(),
                                (truck, sensors) -> RouteMapper.toDomain(entity, truck, sensors)
                        )
                );
    }

    @Override
    public Flux<Route> findAll() {
        return routeEntityRepository.findAll()
                .flatMap(entity ->
                        Mono.zip(
                                truckRepository.findById(entity.getTruckId()),
                                Flux.fromIterable(entity.getSensorIds())
                                        .flatMap(sensorRepository::findById)
                                        .collectList(),
                                (truck, sensors) -> RouteMapper.toDomain(entity, truck, sensors)
                        )
                );
    }

    @Override
    public Mono<Route> save(Route route) {
        RouteEntity entity = RouteMapper.toEntity(route);
        return routeEntityRepository.save(entity)
                .flatMap(savedEntity ->
                        Mono.zip(
                                truckRepository.findById(savedEntity.getTruckId()),
                                Flux.fromIterable(savedEntity.getSensorIds())
                                        .flatMap(sensorRepository::findById)
                                        .collectList(),
                                (truck, sensors) -> RouteMapper.toDomain(savedEntity, truck, sensors)
                        )
                );
    }

}