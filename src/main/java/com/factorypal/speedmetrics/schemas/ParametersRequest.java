package com.factorypal.speedmetrics.schemas;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParametersRequest {

    private String machineKey;
    private JsonNode parameters;
}
