package com.factorypal.speedmetrics;

import com.factorypal.speedmetrics.services.SeederService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Profile("!test")
public class SeedApplicationRunner implements ApplicationRunner {

    private final SeederService seederService;

    public SeedApplicationRunner(SeederService seederService) {
        this.seederService = seederService;
    }

    @Override
    public void run(ApplicationArguments args) {
        seederService.seedMachines();
        seederService.seedMachineParameters();
    }
}
