package com.ub.higiea.infrastructure.controller;

import com.ub.higiea.application.domainservice.RouteService;
import com.ub.higiea.application.dtos.RouteDTO;
import com.ub.higiea.application.dtos.SensorDTO;
import com.ub.higiea.application.dtos.TruckDTO;
import com.ub.higiea.domain.model.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@WebFluxTest(controllers = RouteController.class)
public class RouteControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private RouteService routeService;

    @Test
    void getAllRoutes_ShouldReturnListOfRouteDTOs() {

        Sensor sensor1 = Sensor.create(1L, Location.create(20.0, 10.0), ContainerState.EMPTY);
        Sensor sensor2 = Sensor.create(2L, Location.create(30.0, 60.0), ContainerState.EMPTY);
        Truck truck = Truck.create(1L);

        Route route1 = Route.create("1", truck, List.of(sensor1,sensor2),100.0,50.0);
        Route route2 = Route.create("2", truck, List.of(sensor2,sensor1),500.0,100.0);
        RouteDTO routeDTO1 = RouteDTO.fromRoute(route1);
        RouteDTO routeDTO2 = RouteDTO.fromRoute(route2);

        Mockito.when(routeService.getAllRoutes()).thenReturn(Flux.just(routeDTO1, routeDTO2));

        webClient.get()
                .uri("/routes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RouteDTO.class)
                .hasSize(2)
                .contains(routeDTO1, routeDTO2);
    }

    @Test
    void getRouteById_ShouldReturnRouteDTO_WhenRouteExists() {
        Sensor sensor1 = Sensor.create(1L, Location.create(20.0, 10.0), ContainerState.EMPTY);
        Sensor sensor2 = Sensor.create(2L, Location.create(30.0, 60.0), ContainerState.EMPTY);
        Truck truck = Truck.create(1L);

        Route route = Route.create("1", truck, List.of(sensor1,sensor2),100.0,50.0);
        RouteDTO routeDTO = RouteDTO.fromRoute(route);

        Mockito.when(routeService.getRouteById()).thenReturn(Mono.just(routeDTO));

    }

    @Test
    void createRoute_ShouldReturnCreatedRouteDTO(){

    }

    @Test
    void getRouteById_ShouldReturnNotFound_WhenRouteDoesNotExists(){

    }

    @Test
    void createRoute_ShouldReturnBadRequest_WhenInputIsInvalid(){

    }

}
