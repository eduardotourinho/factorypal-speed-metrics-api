package com.factorypal.speedmetrics.services;

import com.factorypal.speedmetrics.domain.entities.MachineParameterStatistics;
import com.factorypal.speedmetrics.domain.entities.Parameter;
import com.factorypal.speedmetrics.domain.repositories.MachineParametersRepository;
import com.factorypal.speedmetrics.mappers.ResponseMapper;
import com.factorypal.speedmetrics.schemas.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
@Component
public class MachineParametersService {

    private final MachineParametersRepository repository;

    public MachineParametersService(MachineParametersRepository repository) {
        this.repository = repository;
    }

    public Flux<MachineParametersResponse> saveAll(List<Parameter> parameters) {
        Flux<Parameter> parameterFlux = repository.saveAll(parameters);

        return ResponseMapper.machineParametersToResponse(parameterFlux);
    }

    public Flux<MachineParametersResponse> getLatestParameters() {
        Flux<Parameter> parameterFlux = repository.fetchLatestParameters();

        return ResponseMapper.machineParametersToResponse(parameterFlux);
    }

    public Flux<MachineStatisticsResponse> getStatisticsForLastMinutes(int minutes) {
        Flux<MachineParameterStatistics> statisticsFlux = repository.getMachineParametersStatistics(minutes);

        return ResponseMapper.machineStatisticsToResponse(statisticsFlux);
    }
}
