package com.factorypal.speedmetrics.handlers;

import com.factorypal.speedmetrics.domain.entities.Parameter;
import com.factorypal.speedmetrics.domain.repositories.MachineParametersRepository;
import com.factorypal.speedmetrics.routers.MachineParameterRouter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MachineParameterRouter.class, MachineParametersHandler.class})
@WebFluxTest
class MachineParametersHandlerTest {

    @Autowired
    private ApplicationContext context;

    @MockBean
    private MachineParametersRepository repository;

    private WebTestClient webClient;

    @BeforeEach
    void setUp() {
        webClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void listMachineParameters() {
        List<Parameter> parameterList = List.of(
                Parameter.builder().machineKey("machine1").key("speed").value(30d).build(),
                Parameter.builder().machineKey("machine1").key("latency").value(500d).build(),
                Parameter.builder().machineKey("machine2").key("speed").value(20d).build()
        );

        when(repository.fetchLatestParameters()).thenReturn(Flux.fromIterable(parameterList));

        webClient.get()
                .uri("/parameters")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Parameter.class)
                .value(parameters -> {
                    assertEquals("machine1", parameters.get(0).getMachineKey());
                    assertEquals("speed", parameters.get(0).getKey());
                    assertEquals(30d, parameters.get(0).getValue());

                    assertEquals("machine1", parameters.get(1).getMachineKey());
                    assertEquals("latency", parameters.get(1).getKey());
                    assertEquals(500d, parameters.get(1).getValue());

                    assertEquals("machine2", parameters.get(2).getMachineKey());
                    assertEquals("speed", parameters.get(2).getKey());
                    assertEquals(20d, parameters.get(2).getValue());
                });
    }

    @Test
    void addMachineParameters() {
        String json = "{\"machineKey\": \"embosser\", \"parameters\": {\"core_diameter\": 3, \"speed\": 20 }}";
        when(repository.saveAll(any())).thenReturn(Flux.fromIterable(List.of()));

        webClient.post()
                .uri("/parameters")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(json), String.class)
                .exchange()
                .expectStatus().isCreated();
    }
}