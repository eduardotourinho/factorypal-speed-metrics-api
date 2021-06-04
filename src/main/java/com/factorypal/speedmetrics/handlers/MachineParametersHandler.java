package com.factorypal.speedmetrics.handlers;

import com.factorypal.speedmetrics.mappers.RequestMapper;
import com.factorypal.speedmetrics.schemas.MachineParametersResponse;
import com.factorypal.speedmetrics.schemas.MachineStatisticsResponse;
import com.factorypal.speedmetrics.schemas.ParametersRequest;
import com.factorypal.speedmetrics.services.MachineParametersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.springframework.web.reactive.function.BodyInserters.*;

@Component
@Slf4j
public class MachineParametersHandler {

    @Value("${app.defaultLastMinutesStatistics:1}")
    private int defaultLastMinutesStatistics;

    private final MachineParametersService parametersService;

    public MachineParametersHandler(MachineParametersService parametersService) {
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
        Optional<String> lastMinutesParam = request.queryParam("minutes");
        String lastMinutes = lastMinutesParam.orElseGet(() -> String.valueOf(defaultLastMinutesStatistics));

        return ServerResponse
                .ok().contentType(MediaType.APPLICATION_JSON)
                .body(fromProducer(
                        parametersService.getStatisticsForLastMinutes(Integer.parseInt(lastMinutes)),
                        MachineStatisticsResponse.class
                ))
                .switchIfEmpty(Mono.empty());
    }
}
