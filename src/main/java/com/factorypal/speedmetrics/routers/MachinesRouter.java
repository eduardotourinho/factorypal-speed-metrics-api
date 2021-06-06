package com.factorypal.speedmetrics.routers;

import com.factorypal.speedmetrics.domain.entities.Machine;
import com.factorypal.speedmetrics.domain.repositories.MachineRepository;
import com.factorypal.speedmetrics.handlers.MachinesHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class MachinesRouter {

    @Bean
    @RouterOperation(path = "/machines", operation = @Operation(
            tags = { "machine-services" },
            operationId = "listAllMachines",
            responses = { @ApiResponse(responseCode = "200", description = "List of all available machines", content = @Content(
                    array = @ArraySchema(schema = @Schema(implementation = Machine.class))
            )) }
    ))
    public RouterFunction<ServerResponse> machineRoutes(MachinesHandler machinesHandler) {
        return route(GET("/machines"), machinesHandler::listAllMachines);
    }
}
