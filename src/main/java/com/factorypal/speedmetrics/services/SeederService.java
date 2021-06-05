package com.factorypal.speedmetrics.services;

import com.factorypal.speedmetrics.config.ApplicationConfig;
import com.factorypal.speedmetrics.domain.entities.Machine;
import com.factorypal.speedmetrics.domain.entities.Parameter;
import com.factorypal.speedmetrics.domain.repositories.MachineParametersRepository;
import com.factorypal.speedmetrics.domain.repositories.MachineRepository;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

@Component
public class SeederService {

    private final ApplicationConfig appConfig;
    private final MachineRepository machineRepository;
    private final MachineParametersRepository machineParametersRepository;

    public SeederService(ApplicationConfig appConfig, MachineRepository machineRepository, MachineParametersRepository machineParametersRepository) {
        this.appConfig = appConfig;
        this.machineRepository = machineRepository;
        this.machineParametersRepository = machineParametersRepository;
    }


    public void seed() {
        seedMachines();
        seedMachineParameters();
    }

    private void seedMachines() {
        machineRepository.count()
                .subscribe(
                        s -> {
                            if (s == 0) {
                                var csvReader = new CsvReader<Machine>();
                                try {
                                    List<Machine> machineSeed = csvReader.read(appConfig.getSeedMachineFile(), Machine.class);
                                    machineRepository.insert(machineSeed).blockLast();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );
    }

    private void seedMachineParameters() {
        machineParametersRepository.count()
                .subscribe(
                        s -> {
                            if (s == 0) {
                                var csvReader = new CsvReader<Parameter>();
                                try {
                                    List<Parameter> parameterList = csvReader.read(appConfig.getSeedMachineParameters(), Parameter.class);
                                    parameterList.forEach(parameter -> parameter.setCreatedAt(new Date()));
                                    machineParametersRepository.saveAll(parameterList).blockLast();
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                );
    }
}
