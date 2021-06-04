package com.factorypal.speedmetrics.schemas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParameterResponse {

    private String name;
    private double value;

    private Date updatedAt;
}
