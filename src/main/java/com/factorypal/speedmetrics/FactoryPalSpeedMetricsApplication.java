package com.factorypal.speedmetrics;

import com.factorypal.speedmetrics.services.SeederService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FactoryPalSpeedMetricsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FactoryPalSpeedMetricsApplication.class, args);
    }
}
