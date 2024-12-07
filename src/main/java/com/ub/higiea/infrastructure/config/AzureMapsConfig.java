package com.ub.higiea.infrastructure.config;


import com.azure.identity.AzureCliCredentialBuilder;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.maps.route.MapsRouteAsyncClient;
import com.azure.maps.route.MapsRouteClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureMapsConfig {

    @Value("${azure.maps.client-id}")
    private String mapsClientId;

    @Bean
    public MapsRouteAsyncClient mapsRouteAsyncClient() {
        return new MapsRouteClientBuilder()
                .credential(new AzureCliCredentialBuilder().build())
                .mapsClientId(mapsClientId)
                .buildAsyncClient();
    }
}
