package com.factorypal.speedmetrics.mappers;

import com.factorypal.speedmetrics.domain.entities.Machine;
import com.factorypal.speedmetrics.domain.entities.Parameter;
import com.factorypal.speedmetrics.schemas.ParametersRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestMapperTest {

    @Test
    void toParametersMustReturnAListOfMachineParameters() throws JsonProcessingException {
        String machineKey = "embosser";
        String jsonParameters = "{\"core_diameter\": 3, \"speed\": 20}";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode requestParameters = mapper.readTree(jsonParameters);
        ParametersRequest request = new ParametersRequest(machineKey, requestParameters);

        List<Parameter> parameters = RequestMapper.toParameterList(request);

        assertEquals(2, parameters.size());
        var keys = parameters.stream().map(Parameter::getMachineKey).collect(Collectors.toSet());

        assertEquals(1, keys.size());
        assertTrue(keys.contains(machineKey));

        assertEquals("core_diameter", parameters.get(0).getKey());
        assertEquals(3, parameters.get(0).getValue());

        assertEquals("speed", parameters.get(1).getKey());
        assertEquals(20, parameters.get(1).getValue());
    }
}
