package com.factorypal.speedmetrics.domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
public class Parameter {

    @Id
    private String machineKey;

    private String key;
    private Double value;
    private Date createdAt;
}
