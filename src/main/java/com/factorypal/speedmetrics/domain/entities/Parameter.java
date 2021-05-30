package com.factorypal.speedmetrics.domain.entities;

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
    private ObjectId id;
    private String machineKey;

    private String key;
    private Double value;

    @CreatedDate
    private Date createdAt;
}
