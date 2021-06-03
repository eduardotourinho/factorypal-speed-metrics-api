package com.factorypal.speedmetrics.domain.entities;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class MachineParameterStatistics {

    private String machineKey;
    private String property;

    private double max;
    private double min;
    private double average;
    private double median;
}
