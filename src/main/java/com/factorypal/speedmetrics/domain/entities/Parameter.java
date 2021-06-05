package com.factorypal.speedmetrics.domain.entities;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Parameter {

    @Id
    @CsvIgnore
    private ObjectId id;

    @CsvBindByName(column = "machine_key", required = true)
    private String machineKey;

    @CsvBindByName(required = true)
    private String key;

    @CsvBindByName(required = true)
    private double value;

    @CreatedDate
    private Date createdAt;
}
