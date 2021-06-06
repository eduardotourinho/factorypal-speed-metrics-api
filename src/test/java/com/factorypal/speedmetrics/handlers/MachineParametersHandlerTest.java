package com.factorypal.speedmetrics.handlers;

import com.factorypal.speedmetrics.config.ApplicationConfig;
import com.factorypal.speedmetrics.domain.entities.Parameter;
import com.factorypal.speedmetrics.domain.repositories.MachineParametersRepository;
import com.factorypal.speedmetrics.routers.MachineParameterRouter;
import com.factorypal.speedmetrics.schemas.MachineParametersResponse;
import com.factorypal.speedmetrics.schemas.MachineStatisticsResponse;
import com.factorypal.speedmetrics.services.MachineParametersService;
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
@ContextConfiguration(classes = {MachineParameterRouter.class, MachineParametersHandler.class, MachineParametersService.class, ApplicationConfig.class})
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
                .expectBodyList(MachineParametersResponse.class)
                .value(machines -> {
                    assertEquals("machine1", machines.get(1).getMachine());
                    var machine1Parameters = machines.get(1).getParameters();
                    assertEquals(2, machine1Parameters.size());

                    assertEquals("machine2", machines.get(0).getMachine());
                    var machine2Parameters =  machines.get(0).getParameters();
                    assertEquals(1,machine2Parameters.size());
                });
    }

    @Test
    void addMachineParameters() {
        String json = "{\"machineKey\": \"embosser\", \"parameters\": {\"core_diameter\": 3, \"speed\": 20 }}";
        List<Parameter> parameters = List.of(
                Parameter.builder().machineKey("embosser").key("core_diameter").value(3d).build(),
                Parameter.builder().machineKey("embosser").key("speed").value(20d).build()
        );

        when(repository.saveAll(any())).thenReturn(Flux.fromIterable(parameters));

        webClient.post()
                .uri("/parameters")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(json), String.class)
                .exchange()
                .expectStatus().isCreated();
    }
}