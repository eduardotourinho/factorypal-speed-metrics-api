package com.factorypal.speedmetrics.services;

import com.factorypal.speedmetrics.config.ApplicationConfig;
import com.factorypal.speedmetrics.domain.entities.Machine;
import com.factorypal.speedmetrics.domain.entities.Parameter;
import com.factorypal.speedmetrics.domain.repositories.MachineParametersRepository;
import com.factorypal.speedmetrics.domain.repositories.MachineRepository;
import com.factorypal.speedmetrics.infrastructure.repositories.MachineParametersRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles(profiles = "test")
class SeederServiceTest {

    @Mock
    private ApplicationConfig config;

    @Autowired
    private ReactiveMongoOperations mongoOperations;

    @Autowired
    private MachineRepository machineRepository;
    private MachineParametersRepository machineParametersRepository;

    private SeederService seederService;

    @BeforeEach
    void setUp() {
        machineParametersRepository = new MachineParametersRepositoryImpl(mongoOperations);
        seederService = new SeederService(config, machineRepository, machineParametersRepository);
    }

    @Test
    void seedMachineShouldSeedDB() {
        mongoOperations.dropCollection(Machine.class).block();
        assertEquals(0, machineRepository.count().block());
        Mockito.when(config.getSeedMachineFile()).thenReturn("src/test/resources/data/machines.csv");

        seederService.seedMachines();

        assertEquals(3, machineRepository.count().block());
    }

    @Test
    void seedMachineShouldNotSeedDB() {
        mongoOperations.dropCollection(Machine.class).block();
        mongoOperations.insert(Machine.builder().key("test").name("Test").build()).block();
        assertEquals(1, machineRepository.count().block());

        Mockito.when(config.getSeedMachineFile()).thenReturn("src/test/resources/data/machines.csv");
        seederService.seedMachines();

        assertEquals(1, machineRepository.count().block());
    }

    @Test
    void seedMachineParametersShouldSeedDB() {
        mongoOperations.dropCollection(Parameter.class).block();
        assertEquals(0, machineParametersRepository.count().block());
        Mockito.when(config.getSeedMachineParameters()).thenReturn("src/test/resources/data/parameters.csv");

        seederService.seedMachineParameters();

        assertEquals(8, machineParametersRepository.count().block());
    }

    @Test
    void seedMachineParametersShouldNotSeedDB() {
        mongoOperations.dropCollection(Parameter.class).block();
        var parameter = Parameter.builder().machineKey("test").key("speed").value(20).createdAt(new Date()).build();
        mongoOperations.insert(parameter).block();

        assertEquals(1, machineParametersRepository.count().block());
        Mockito.when(config.getSeedMachineParameters()).thenReturn("src/test/resources/data/parameters.csv");

        seederService.seedMachineParameters();

        assertEquals(1, machineParametersRepository.count().block());
    }
}
