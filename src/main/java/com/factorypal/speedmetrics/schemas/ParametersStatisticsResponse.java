package com.factorypal.speedmetrics.schemas;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ParametersStatisticsResponse {

    private String property;
    private StatisticsResponse statistics;
}
