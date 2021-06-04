package com.factorypal.speedmetrics.schemas;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatisticsResponse {

    private double min;
    private double max;
    private double avg;
    private double median;
}
