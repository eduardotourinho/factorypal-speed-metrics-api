package com.factorypal.speedmetrics.routers;

import com.factorypal.speedmetrics.handlers.MachinesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class MachinesRouter {

    @Bean
    public RouterFunction<ServerResponse> machineRoutes(MachinesHandler machinesHandler) {
        return route()
                .path("/machines", builder -> builder
                        .GET("", accept(MediaType.APPLICATION_JSON), machinesHandler::listAllMachines)
                )
                .build();
    }
}
