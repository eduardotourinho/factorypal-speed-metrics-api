package com.factorypal.speedmetrics.handlers;

import com.factorypal.speedmetrics.FactoryPalSpeedMetricsApplication;
import com.factorypal.speedmetrics.domain.entities.Machine;
import com.factorypal.speedmetrics.domain.repositories.MachineRepository;
import com.factorypal.speedmetrics.routers.MachineParameterRouter;
import com.factorypal.speedmetrics.routers.MachinesRouter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MachinesRouter.class, MachinesHandler.class})
@WebFluxTest
@EnableReactiveMongoRepositories
class MachinesHandlerTest {

    @Autowired
    private ApplicationContext context;

    @MockBean
    private MachineRepository repository;

    private WebTestClient webClient;

    @BeforeEach
    void setUp() {
        webClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    void listAllMachines() {
        List<Machine> machines = List.of(
                Machine.builder().key("aufwickler").name("Aufwickler").build(),
                Machine.builder().key("wickelkopf").name("Wickelkopf").build(),
                Machine.builder().key("ajoparametrit").name("Ajoparametrit").build()
        );

        when(repository.findAll()).thenReturn(Flux.fromIterable(machines));

        webClient.get()
                .uri("/machines")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Machine.class)
                .value(machineList -> {
                    assertEquals(3, machineList.size());

                    assertEquals("aufwickler", machineList.get(0).getKey());
                    assertEquals("Aufwickler", machineList.get(0).getName());
                });
    }
}