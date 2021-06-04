package com.factorypal.speedmetrics.mappers;

import com.factorypal.speedmetrics.domain.entities.MachineParameterStatistics;
import com.factorypal.speedmetrics.domain.entities.Parameter;
import com.factorypal.speedmetrics.schemas.*;
import reactor.core.publisher.Flux;

import java.util.stream.Collectors;

public class ResponseMapper {

    public static Flux<MachineParametersResponse> machineParametersToResponse(Flux<Parameter> parameterFlux) {
        return parameterFlux
                .collect(
                        Collectors.groupingBy(
                                Parameter::getMachineKey,
                                Collectors.mapping(
                                        p -> new ParameterResponse(
                                                p.getKey(),
                                                p.getValue(),
                                                p.getCreatedAt()
                                        ), Collectors.toList())
                        )
                )
                .flatMapIterable(
                        grouping -> grouping
                                .entrySet().stream()
                                .map(m -> new MachineParametersResponse(m.getKey(), m.getValue()))
                                .collect(Collectors.toList())
                );

    }

    public static Flux<MachineStatisticsResponse> machineStatisticsToResponse(Flux<MachineParameterStatistics> statisticsFlux) {
        return statisticsFlux
                .collect(
                        Collectors.groupingBy(
                                MachineParameterStatistics::getMachineKey,
                                Collectors.mapping(
                                        parameterStatistic -> new ParametersStatisticsResponse(
                                                parameterStatistic.getProperty(),
                                                new StatisticsResponse(
                                                        parameterStatistic.getMin(),
                                                        parameterStatistic.getMax(),
                                                        parameterStatistic.getAverage(),
                                                        parameterStatistic.getMedian())
                                        ), Collectors.toList())

                        )
                )
                .flatMapIterable(
                        grouping -> grouping
                                .entrySet().stream()
                                .map(m -> new MachineStatisticsResponse(m.getKey(), m.getValue()))
                                .collect(Collectors.toList())
                );
    }
}
