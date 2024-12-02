package com.ub.higiea.infrastructure.controller;

import com.ub.higiea.application.services.MessageService;
import com.ub.higiea.application.services.domain.RouteService;
import com.ub.higiea.application.dtos.RouteDTO;
import com.ub.higiea.application.exception.notfound.RouteNotFoundException;
import com.ub.higiea.application.requests.RouteCreateRequest;
import com.ub.higiea.infrastructure.utils.GeoJsonUtils;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("routes")
@Validated
public class RouteController {

    private final RouteService routeService;
    private final MessageService messageService;

    public RouteController(RouteService routeService, MessageService messageService) {
        this.routeService = routeService;
        this.messageService = messageService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> getAllRoutes() {
        return routeService.getAllRoutes()
                .map(RouteDTO::toGeoJSON)
                .collectList()
                .map(GeoJsonUtils::combineRoutes);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> getRoute(@PathVariable("id") String routeId) {
        return routeService.getRouteById(routeId)
                .map(RouteDTO::toGeoJSON)
                .onErrorMap(RouteNotFoundException.class, ex ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(),ex));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> createRoute(@Valid @RequestBody RouteCreateRequest request) {
        return messageService.createRoute(request)
                .map(RouteDTO::toGeoJSON)
                .onErrorMap(ValidationException.class, ex ->
                        new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage(), ex));
    }
}
