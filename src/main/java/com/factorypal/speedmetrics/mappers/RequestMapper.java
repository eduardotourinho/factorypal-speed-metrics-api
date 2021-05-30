package com.factorypal.speedmetrics.mappers;

import com.factorypal.speedmetrics.domain.entities.Parameter;
import com.factorypal.speedmetrics.schemas.ParametersRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class RequestMapper {

    public static List<Parameter> toParameterList(ParametersRequest request) {
        ObjectMapper mapper = new ObjectMapper();
        String machineKey = request.getMachineKey();
        JsonNode requestParameters = request.getParameters();

        Map<String, Double> results = mapper.convertValue(requestParameters, new TypeReference<>() { });

        return results.entrySet().stream()
                .map(entry -> Parameter.builder()
                        .machineKey(machineKey)
                        .key(entry.getKey())
                        .value(entry.getValue())
                        .createdAt(new Date())
                        .build())
                .collect(Collectors.toUnmodifiableList());
    }
}
