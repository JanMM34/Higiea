package com.ub.higiea.infrastructure.controller;

import com.ub.higiea.application.domainservice.RouteService;
import com.ub.higiea.application.dtos.RouteDTO;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("routes")
@Validated
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<RouteDTO> getAllRoutes() {
        return routeService.getAllRoutes();
    }

}
