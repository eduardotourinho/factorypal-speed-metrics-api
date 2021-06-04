package com.factorypal.speedmetrics.schemas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MachineParametersResponse {

    private String machine;
    private List<ParameterResponse> parameters;
}
