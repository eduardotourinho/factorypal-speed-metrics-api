package com.factorypal.speedmetrics.infrastructure;

import com.factorypal.speedmetrics.domain.entities.Machine;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvReaderTest {

    @Test
    void readShouldReturnAListOfMachines() {
        var testCsvData = "src/test/resources/data/machines.csv";
        var csvReader = new CsvReader<Machine>();

        assertDoesNotThrow(() -> {
            List<Machine> machines = csvReader.read(testCsvData, Machine.class);
            assertEquals(3, machines.size());

            machines.forEach(machine -> {
                assertNotNull(machine.getKey());
                assertNotNull(machine.getName());
            });
        });
    }

    @Test
    void readShouldThrowRuntimeException() {
        var testCsvData = "src/test/resources/data/incorrect_data.csv";
        assertThrows(RuntimeException.class, () -> {
            var csvReader = new CsvReader<Machine>();

            var machineList = csvReader.read(testCsvData, Machine.class);
            assertNull(machineList);
        });
    }

    @Test
    void readShouldThrowFileNotFoundException() {
        var testCsvData = "non_existent.csv";
        assertThrows(FileNotFoundException.class, () -> {
            var csvReader = new CsvReader<Machine>();

            var machineList = csvReader.read(testCsvData, Machine.class);
            assertNull(machineList);
        });
    }
}