package com.factorypal.speedmetrics.schemas;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MachineStatisticsResponse {
    private String machine;
    private List<ParametersStatisticsResponse> parameters;
}
