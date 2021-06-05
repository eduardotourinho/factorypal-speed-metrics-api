package com.factorypal.speedmetrics.services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.*;
import java.util.List;

public class CsvReader<T> {

    public List<T> read(String csvFilePath, Class<T> cls) throws FileNotFoundException {
        FileReader reader = new FileReader(csvFilePath);
        CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                .withType(cls)
                .withIgnoreLeadingWhiteSpace(true)
                .withIgnoreQuotations(true)
                .withIgnoreEmptyLine(true)
                .build();

        return csvToBean.parse();
    }
}
