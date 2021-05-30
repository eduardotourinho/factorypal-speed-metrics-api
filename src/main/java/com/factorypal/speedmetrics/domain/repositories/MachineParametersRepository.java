package com.factorypal.speedmetrics.domain.repositories;

import com.factorypal.speedmetrics.domain.entities.Parameter;
import reactor.core.publisher.Flux;

import java.util.Collection;

public interface MachineParametersRepository {

    Flux<Parameter> saveAll(Collection<Parameter> parameters);

    Flux<Parameter> fetchLatestParameters();
}
