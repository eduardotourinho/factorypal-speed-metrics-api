package com.factorypal.speedmetrics;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles(profiles = {"test"})
class FactoryPalSpeedMetricsApplicationTests {

    @Autowired
    private FactoryPalSpeedMetricsApplication app;

    @Test
    void contextLoads() {
        assertNotNull(app);
    }
}
