package com.factorypal.speedmetrics.handlers;

import com.factorypal.speedmetrics.config.ApplicationConfig;
import com.factorypal.speedmetrics.mappers.RequestMapper;
import com.factorypal.speedmetrics.schemas.MachineParametersResponse;
import com.factorypal.speedmetrics.schemas.MachineStatisticsResponse;
import com.factorypal.speedmetrics.schemas.ParametersRequest;
import com.factorypal.speedmetrics.services.MachineParametersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.*;

@Component
@Slf4j
public class MachineParametersHandler {

    private final ApplicationConfig appConfig;
    private final MachineParametersService parametersService;

    public MachineParametersHandler(ApplicationConfig appConfig, MachineParametersService parametersService) {
        this.appConfig = appConfig;
        this.parametersService = parametersService;
    }

    public Mono<ServerResponse> listMachineParameters(ServerRequest request) {
        return ServerResponse
                .ok().contentType(MediaType.APPLICATION_JSON)
                .body(fromProducer(
                        parametersService.getLatestParameters(),
                        MachineParametersResponse.class
                ))
                .switchIfEmpty(Mono.empty());
    }

    public Mono<ServerResponse> addMachineParameters(ServerRequest request) {
        return request.bodyToMono(ParametersRequest.class)
                .map(RequestMapper::toParameterList)
                .map(parametersService::saveAll)
                .flatMap(saveResponse -> ServerResponse
                        .created(request.uri()).contentType(MediaType.APPLICATION_JSON)
                        .body(fromProducer(saveResponse, MachineParametersResponse.class))
                )
                .switchIfEmpty(Mono.empty());
    }

    public Mono<ServerResponse> getMachineParametersStatistics(ServerRequest request) {
        String lastMinutesQp = request.queryParam("minutes")
                .orElseGet(appConfig::getStatisticsFromDefaultMinutes);

        return ServerResponse
                .ok().contentType(MediaType.APPLICATION_JSON)
                .body(fromProducer(
                        parametersService.getStatisticsForLastMinutes(Integer.parseInt(lastMinutesQp)),
                        MachineStatisticsResponse.class
                ))
                .switchIfEmpty(Mono.empty());
    }
}
