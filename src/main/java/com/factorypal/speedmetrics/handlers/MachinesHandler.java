package com.factorypal.speedmetrics.handlers;

import com.factorypal.speedmetrics.domain.entities.Machine;
import com.factorypal.speedmetrics.domain.repositories.MachineRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromPublisher;

@Component
public class MachinesHandler {

    private final MachineRepository machineRepository;

    public MachinesHandler(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    public Mono<ServerResponse> listAllMachines(ServerRequest request) {
        return ServerResponse
                .ok().contentType(MediaType.APPLICATION_JSON)
                .body(fromPublisher(machineRepository.findAll(), Machine.class));
    }
}
