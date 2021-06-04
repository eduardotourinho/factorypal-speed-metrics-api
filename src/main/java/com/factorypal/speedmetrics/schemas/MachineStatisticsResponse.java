package com.factorypal.speedmetrics.schemas;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MachineStatisticsResponse {
    private String key;
    private List<ParametersStatisticsResponse> parameters;
}
