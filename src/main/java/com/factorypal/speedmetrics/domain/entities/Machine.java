package com.factorypal.speedmetrics.domain.entities;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Machine {

    @CsvBindByName(required = true)
    private String key;

    @CsvBindByName(required = true)
    private String name;
}
