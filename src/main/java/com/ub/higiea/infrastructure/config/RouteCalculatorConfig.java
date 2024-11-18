package com.ub.higiea.infrastructure.config;

import com.azure.maps.route.MapsRouteAsyncClient;
import com.ub.higiea.application.utils.RouteCalculator;
import com.ub.higiea.infrastructure.adapters.azuremaps.AzureMapsRouteCalculator;
import com.ub.higiea.infrastructure.adapters.MockRouteCalculatorImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteCalculatorConfig {

    @Value("${route.calculator.type:mock}")
    private String calculatorType;

    private final MapsRouteAsyncClient mapsRouteAsyncClient;

    public RouteCalculatorConfig(MapsRouteAsyncClient mapsRouteAsyncClient) {
        this.mapsRouteAsyncClient = mapsRouteAsyncClient;
    }

    @Bean
    public RouteCalculator routeCalculator() {
        switch (calculatorType) {
            case "azure":
                return new AzureMapsRouteCalculator(mapsRouteAsyncClient);
            case "mock":
            default:
                return new MockRouteCalculatorImpl();
        }
    }

}
