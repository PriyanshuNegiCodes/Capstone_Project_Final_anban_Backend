package com.example.APIGateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p.path("/api/v1/auth/**")
                        .uri("http://localhost:3033/"))
                .route(p -> p.path("/api/v1/project/**")
                        .uri("http://localhost:8007/"))
                .route(p -> p.path("/api/v1/user/**")
                        .uri("http://localhost:8007/"))
                .route(p -> p.path("/api/v1/notifications/**")
                        .uri("http://localhost:8090/"))
                .build();
    }
}


//way to do the load balancing is mentioned below, the service names are incorrrect
//        return routeLocatorBuilder.routes()
//                .route(p -> p.path("/api/v1/auth/**")
//                        .uri("lb://AUTH-SERVICE"))  // Replace with actual Eureka service name
//                .route(p -> p.path("/api/v1/project/**")
//                        .uri("lb://PROJECT-SERVICE"))  // Replace with actual Eureka service name
//                .route(p -> p.path("/api/v1/user/**")
//                        .uri("lb://USER-SERVICE"))  // Replace with actual Eureka service name
//                .route(p -> p.path("/api/v1/notifications/**")
//                        .uri("lb://NOTIFICATIONS-SERVICE"))  // Replace with actual Eureka service name
//                .build();
//    }