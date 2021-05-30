package com.factorypal.speedmetrics.handlers;

import com.factorypal.speedmetrics.domain.entities.Machine;
import com.factorypal.speedmetrics.domain.entities.Parameter;
import com.factorypal.speedmetrics.domain.repositories.MachineParametersRepository;
import com.factorypal.speedmetrics.mappers.RequestMapper;
import com.factorypal.speedmetrics.schemas.ParametersRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

@Component
@Slf4j
public class MachineParametersHandler {

    private final MachineParametersRepository parametersService;

    public MachineParametersHandler(MachineParametersRepository parametersService) {
        this.parametersService = parametersService;
    }

    public Mono<ServerResponse> listMachineParameters(ServerRequest request) {
        return ServerResponse
                .ok().contentType(MediaType.APPLICATION_JSON)
                .body(fromPublisher(parametersService.fetchLatestParameters(), Parameter.class));
    }

    public Mono<ServerResponse> addMachineParameters(ServerRequest request) {
        Mono<ParametersRequest> parametersRequest = request.bodyToMono(ParametersRequest.class);

        return parametersRequest
                .flatMap(reqParam -> {
                    List<Parameter> mappedParams = RequestMapper.toParameterList(reqParam);
                    Flux<Parameter> insertedParams = parametersService.saveAll(mappedParams);
                    insertedParams.doOnNext(parameter -> log.info(String.format("inserted: %s", parameter.getId())));

                    var body = fromPublisher(insertedParams, Parameter.class);

                    return ServerResponse
                            .created(request.uri()).contentType(MediaType.APPLICATION_JSON)
                            .body(body);
                });
    }
}
