package com.factorypal.speedmetrics.services;

import com.factorypal.speedmetrics.config.ApplicationConfig;
import com.factorypal.speedmetrics.domain.entities.Machine;
import com.factorypal.speedmetrics.domain.entities.Parameter;
import com.factorypal.speedmetrics.domain.repositories.MachineParametersRepository;
import com.factorypal.speedmetrics.domain.repositories.MachineRepository;
import com.factorypal.speedmetrics.infrastructure.CsvReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class SeederService {

    private final ApplicationConfig appConfig;
    private final MachineRepository machineRepository;
    private final MachineParametersRepository machineParametersRepository;

    public SeederService(ApplicationConfig appConfig, MachineRepository machineRepository, MachineParametersRepository machineParametersRepository) {
        this.appConfig = appConfig;
        this.machineRepository = machineRepository;
        this.machineParametersRepository = machineParametersRepository;
    }

    public void seedMachines() {
        Long count = machineRepository.count().block();
        if (count == null || count > 0) {
            return;
        }

        log.debug("Seeding machines collection");
        var csvReader = new CsvReader<Machine>();
        try {
            List<Machine> machineSeed = csvReader.read(appConfig.getSeedMachineFile(), Machine.class);
            machineRepository.insert(machineSeed).blockLast();
        } catch (FileNotFoundException e) {
            log.error("Could not import machine seed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void seedMachineParameters() {
        Long count = machineParametersRepository.count().block();
        if (count == null || count > 0) {
            return;
        }

        log.debug("Seeding parameters collection");
        var csvReader = new CsvReader<Parameter>();
        try {
            List<Parameter> parameterList = csvReader.read(appConfig.getSeedMachineParameters(), Parameter.class);
            parameterList.forEach(parameter -> parameter.setCreatedAt(new Date()));
            machineParametersRepository.saveAll(parameterList).log(log.getName()).blockLast();

        } catch (FileNotFoundException e) {
            log.error("Could not import parameters seed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
