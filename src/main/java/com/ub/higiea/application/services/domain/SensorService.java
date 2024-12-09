package com.ub.higiea.application.services.domain;

import com.ub.higiea.application.dtos.SensorDTO;
import com.ub.higiea.application.requests.SensorCreateRequest;
import com.ub.higiea.application.exception.notfound.SensorNotFoundException;
import com.ub.higiea.domain.model.ContainerState;
import com.ub.higiea.domain.model.Location;
import com.ub.higiea.domain.model.Sensor;
import com.ub.higiea.domain.repository.SensorRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public Flux<SensorDTO> getAllSensors() {
        return sensorRepository.findAll()
                .map(SensorDTO::fromSensor);
    }

    public Mono<SensorDTO> getSensorById(Long id) {
        return sensorRepository.findById(id)
                .switchIfEmpty(Mono.error(new SensorNotFoundException(id)))
                .map(SensorDTO::fromSensor);
    }

    public Flux<Sensor> getSensorsByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(
                        id -> sensorRepository.findById(id)
                        .switchIfEmpty(Mono.error(new SensorNotFoundException(id)))
                );
    }

    public Mono<SensorDTO> createSensor(SensorCreateRequest request) {
        Sensor sensor = Sensor.create(
                null,
                Location.create(request.getLatitude(), request.getLongitude()),
                ContainerState.valueOf(request.getContainerState())
        );
        return sensorRepository.save(sensor)
                .map(SensorDTO::fromSensor);
    }

    public Mono<Sensor> updateSensorState(Long sensorId, int state) {
        return sensorRepository.findById(sensorId)
                .switchIfEmpty(Mono.error(new SensorNotFoundException(sensorId)))
                .flatMap(sensor -> {
                    sensor.setContainerState(ContainerState.fromLevel(state));
                    return sensorRepository.save(sensor);
                });
    }

    public Flux<Sensor> fetchRelevantSensors(int capacity) {
        return sensorRepository.findAll()
                .filter(sensor -> !sensor.isAssignedToRoute() && sensor.getContainerState() != ContainerState.EMPTY)
                .sort(Comparator.comparingInt(sensor -> -sensor.getContainerState().getLevel()))
                .take(capacity);
    }

    public Mono<Void> saveAll(List<Sensor> sensors) {
        return Flux.fromIterable(sensors)
                .flatMap(sensorRepository::save)
                .then();
    }

}
