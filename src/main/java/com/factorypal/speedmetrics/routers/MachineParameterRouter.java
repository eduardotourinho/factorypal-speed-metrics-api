package com.factorypal.speedmetrics.routers;

import com.factorypal.speedmetrics.handlers.MachineParametersHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;

import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class MachineParameterRouter {

    @Bean
    public RouterFunction<ServerResponse> parametersRoute(MachineParametersHandler machineParametersHandler) {
        return route()
                .path("/parameters", builder -> builder
                        .GET("/statistics", accept(MediaType.APPLICATION_JSON), machineParametersHandler::getMachineParametersStatistics)
                        .GET(machineParametersHandler::listMachineParameters)
                        .POST(accept(MediaType.APPLICATION_JSON), machineParametersHandler::addMachineParameters)
                )
                .build();
    }
}
