package com.factorypal.speedmetrics.routers;

import com.factorypal.speedmetrics.handlers.MachineParametersHandler;
import com.factorypal.speedmetrics.schemas.MachineParametersResponse;
import com.factorypal.speedmetrics.schemas.ParametersRequest;
import com.factorypal.speedmetrics.services.MachineParametersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;

import org.springframework.web.reactive.function.server.ServerResponse;


import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class MachineParameterRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/parameters", method = {RequestMethod.GET}, beanClass = MachineParametersService.class, beanMethod = "getLatestParameters"),
            @RouterOperation(path = "/parameters/statistics", beanClass = MachineParametersService.class, beanMethod = "getStatisticsForLastMinutes"),
            @RouterOperation(path = "/parameters", method = {RequestMethod.POST}, operation = @Operation(
                    tags = {"machine-parameters-service"},
                    operationId = "saveParameters",
                    requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = ParametersRequest.class, example = "{'machineKey': 'embosser', 'parameters': { 'speed': 30, 'core_values': 2}}"))),
                    responses = { @ApiResponse(responseCode = "200", description = "List of inserted parameters", content = @Content(array = @ArraySchema(schema = @Schema(implementation = MachineParametersResponse.class)))) }
            ))
    })
    public RouterFunction<ServerResponse> parametersRoute(MachineParametersHandler machineParametersHandler) {
        return route(GET("/parameters/statistics"), machineParametersHandler::getMachineParametersStatistics)
                .andRoute(GET("/parameters"), machineParametersHandler::listMachineParameters)
                .andRoute(POST("/parameters").and(accept(MediaType.APPLICATION_JSON)), machineParametersHandler::addMachineParameters);
    }
}
