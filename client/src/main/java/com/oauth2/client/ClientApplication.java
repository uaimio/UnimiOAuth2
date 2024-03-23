package com.oauth2.client;

import org.springframework.boot.SpringApplication;
// import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    // @Bean
    // RouteLocator gateway(RouteLocatorBuilder rlb) {
    //     return rlb
    //             .routes()
    //             .route(rs -> rs
    //                     .path("/profile/notes")
    //                     .filters(GatewayFilterSpec::tokenRelay)
    //                     .uri("http://localhost:8081"))
    //             .route(r -> r
    //                     .path("/notes")
    //                     .uri("http://localhost:8081"))
    //             .route(r -> r
    //                     .path("/profile/notes/**")
    //                     .filters(GatewayFilterSpec::tokenRelay)
    //                     .uri("http://localhost:8081"))
    //             .build();
    // }
}
