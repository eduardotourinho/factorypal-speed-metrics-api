package com.factorypal.speedmetrics.infrastructure.repositories;

import com.factorypal.speedmetrics.domain.entities.Parameter;
import com.factorypal.speedmetrics.domain.repositories.MachineParametersRepository;
import com.factorypal.speedmetrics.domain.entities.MachineParameterStatistics;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "test")
class MachineParametersRepositoryImplTest {

    @Autowired
    private ReactiveMongoOperations mongoOperations;

    private MachineParametersRepository repository;

    @BeforeEach
    void setUp() {
        mongoOperations.createCollection(Parameter.class).block();
        repository = new MachineParametersRepositoryImpl(mongoOperations);
    }

    @AfterEach
    void clean() {
        mongoOperations.dropCollection(Parameter.class).block();
    }

    @Test
    void saveAll() {
        Date now = new Date();
        List<Parameter> parameters = List.of(
                Parameter.builder().machineKey("machine1").key("speed").value(1d).createdAt(now).build(),
                Parameter.builder().machineKey("machine1").key("speed").value(2d).createdAt(now).build(),
                Parameter.builder().machineKey("machine1").key("speed").value(3d).createdAt(now).build()
        );
        Flux<Parameter> inserted = repository.saveAll(parameters);

        StepVerifier
                .create(inserted)
                .assertNext(parameter -> {
                    assertNotNull(parameter);
                    assertNotNull(parameter.getId());
                    assertEquals("machine1", parameter.getMachineKey());
                    assertEquals("speed", parameter.getKey());
                    assertEquals(now, parameter.getCreatedAt());
                    assertEquals(1d, parameter.getValue());
                })
                .assertNext(parameter -> {
                    assertNotNull(parameter);
                    assertNotNull(parameter.getId());
                    assertEquals("machine1", parameter.getMachineKey());
                    assertEquals("speed", parameter.getKey());
                    assertEquals(now, parameter.getCreatedAt());
                    assertEquals(2d, parameter.getValue());
                })
                .assertNext(parameter -> {
                    assertNotNull(parameter);
                    assertNotNull(parameter.getId());
                    assertEquals("machine1", parameter.getMachineKey());
                    assertEquals("speed", parameter.getKey());
                    assertEquals(now, parameter.getCreatedAt());
                    assertEquals(3d, parameter.getValue());
                })
                .expectComplete()
                .log()
                .verify();

    }

    @Test
    void fetchLatestParameters() throws ParseException {
        Date now = new Date();
        Date old = new SimpleDateFormat("yyyy-MM-dd").parse("2021-05-30");
        Date older = new SimpleDateFormat("yyyy-MM-dd").parse("2021-05-20");

        List<Parameter> parameters = List.of(
                Parameter.builder().machineKey("machine1").key("speed").value(10d).createdAt(old).build(),
                Parameter.builder().machineKey("machine1").key("speed").value(2d).createdAt(older).build(),
                Parameter.builder().machineKey("machine1").key("speed").value(3d).createdAt(now).build()
        );
        repository.saveAll(parameters).blockLast();

        Flux<Parameter> latestParameter = repository.fetchLatestParameters();

        StepVerifier
                .create(latestParameter)
                .assertNext(latest -> {
                    assertNotNull(latest);
                    assertEquals("machine1", latest.getMachineKey());
                    assertEquals("speed", latest.getKey());
                    assertEquals(3d, latest.getValue());
                })
                .expectComplete()
                .log()
                .verify();
    }

    @Test
    void getMachineParameterStatistics() {
        Date now = new Date();
        List<Parameter> parameters = List.of(
                Parameter.builder().machineKey("machine1").key("speed").value(1d).createdAt(now).build(),
                Parameter.builder().machineKey("machine1").key("speed").value(5d).createdAt(now).build(),
                Parameter.builder().machineKey("machine1").key("speed").value(30d).createdAt(now).build(),
                Parameter.builder().machineKey("machine1").key("speed").value(9d).createdAt(now).build(),
                Parameter.builder().machineKey("machine1").key("speed").value(35d).createdAt(now).build(),

                Parameter.builder().machineKey("machine1").key("load").value(1d).createdAt(now).build(),
                Parameter.builder().machineKey("machine1").key("load").value(5d).createdAt(now).build(),
                Parameter.builder().machineKey("machine1").key("load").value(30d).createdAt(now).build(),
                Parameter.builder().machineKey("machine1").key("load").value(9d).createdAt(now).build(),

                Parameter.builder().machineKey("machine2").key("load").value(1d).createdAt(now).build(),
                Parameter.builder().machineKey("machine2").key("load").value(5d).createdAt(now).build(),
                Parameter.builder().machineKey("machine2").key("load").value(30d).createdAt(now).build(),
                Parameter.builder().machineKey("machine2").key("load").value(9d).createdAt(now).build()
        );
        repository.saveAll(parameters).blockLast();

        Flux<MachineParameterStatistics> statistics = repository.getMachineParametersStatistics(10);

        StepVerifier
                .create(statistics)
                .assertNext(statistic -> {
                    assertNotNull(statistic);
                    assertEquals("machine1", statistic.getMachineKey());
                    assertEquals("load", statistic.getProperty());
                    assertEquals(1d, statistic.getMin());
                    assertEquals(30d, statistic.getMax());
                    assertEquals(11.25d, statistic.getAverage());
                    assertEquals(7d, statistic.getMedian());
                })
                .assertNext(statistic -> {
                    assertNotNull(statistic);
                    assertEquals("machine1", statistic.getMachineKey());
                    assertEquals("speed", statistic.getProperty());
                    assertEquals(1d, statistic.getMin());
                    assertEquals(35d, statistic.getMax());
                    assertEquals(16d, statistic.getAverage());
                    assertEquals(9d, statistic.getMedian());
                })
                .assertNext(statistic -> {
                    assertNotNull(statistic);
                    assertEquals("machine2", statistic.getMachineKey());
                    assertEquals("load", statistic.getProperty());
                    assertEquals(1d, statistic.getMin());
                    assertEquals(30d, statistic.getMax());
                    assertEquals(11.25d, statistic.getAverage());
                    assertEquals(7d, statistic.getMedian());
                })
                .expectComplete()
                .log()
                .verify();
    }
}