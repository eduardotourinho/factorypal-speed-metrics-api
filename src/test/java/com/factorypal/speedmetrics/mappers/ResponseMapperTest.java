package com.factorypal.speedmetrics.mappers;

import com.factorypal.speedmetrics.domain.entities.Parameter;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResponseMapperTest {

    @Test
    void machineParametersToResponse() {
        Flux<Parameter> parameterFlux = Flux.fromIterable(
                List.of(
                        Parameter.builder().machineKey("machine1").key("speed").value(10d).createdAt(new Date()).build(),
                        Parameter.builder().machineKey("machine1").key("core").value(2d).createdAt(new Date()).build(),
                        Parameter.builder().machineKey("machine1").key("load").value(3d).createdAt(new Date()).build()
                )
        );

        var response = ResponseMapper.machineParametersToResponse(parameterFlux);
        StepVerifier
                .create(response)
                .assertNext(
                        machineParametersResponse -> {
                            assertNotNull(machineParametersResponse);

                            assertEquals("machine1", machineParametersResponse.getMachine());
                            var parameters = machineParametersResponse.getParameters();
                            assertEquals(3, parameters.size());
                        }
                )
                .expectComplete()
                .verify();
    }

    @Test
    void machineStatisticsToResponse() {
    }
}