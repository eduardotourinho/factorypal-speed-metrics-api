package com.factorypal.speedmetrics.domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class Machine {

    @Id
    private final String key;
    private final String name;

    @DBRef
    private List<Parameter> parameters;
}
