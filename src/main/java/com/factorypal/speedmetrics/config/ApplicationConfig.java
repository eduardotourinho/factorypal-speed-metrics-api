package com.factorypal.speedmetrics.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ApplicationConfig {

    @Value("${app.defaultLastMinutesStatistics}")
    private String statisticsFromDefaultMinutes;

    @Value("${app.seedMachineFile}")
    private String seedMachineFile;

    @Value("${app.seedParametersFile}")
    private String seedMachineParameters;
}
