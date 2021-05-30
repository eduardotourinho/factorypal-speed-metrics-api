package com.factorypal.speedmetrics.infrastructure.repositories;

import com.factorypal.speedmetrics.domain.repositories.MachineParametersRepository;
import com.factorypal.speedmetrics.domain.repositories.MachineRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ExtendWith(SpringExtension.class)
class MachineParametersRepositoryImplTest {

    private MachineParametersRepository repository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void clean() {

    }

    @Test
    void saveAll() {
    }

    @Test
    void fetchLatestParameters() {
    }
}