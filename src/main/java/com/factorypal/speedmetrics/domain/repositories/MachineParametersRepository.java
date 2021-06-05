package com.factorypal.speedmetrics.domain.repositories;

import com.factorypal.speedmetrics.domain.entities.Parameter;
import com.factorypal.speedmetrics.domain.entities.MachineParameterStatistics;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

public interface MachineParametersRepository {

    Mono<Long> count();

    Flux<Parameter> saveAll(Collection<Parameter> parameters);

    Flux<Parameter> fetchLatestParameters();

    Flux<MachineParameterStatistics> getMachineParametersStatistics(int minutes);
}
