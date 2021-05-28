package com.factorypal.speedmetrics.schemas;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class ParametersRequest {

    private String machineKey;
    private JsonNode parameters;
}
