package com.ub.higiea.infrastructure.adapters.route.trigger;

import com.ub.higiea.application.ports.RouteTrigger;
import com.ub.higiea.domain.model.ContainerState;
import com.ub.higiea.domain.model.Sensor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class FullContainerRouteTrigger implements RouteTrigger {

    @Override
    public Mono<Boolean> shouldTriggerRoute(Sensor sensor) {
        return Mono.just(!sensor.hasAssignedRoute() && sensor.getContainerState() == ContainerState.FULL);
    }

}
