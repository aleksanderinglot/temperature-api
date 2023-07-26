package com.example.temperatureapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TemperatureDataProcessorTest {

    private TemperatureDataProcessor dataProcessor;
    private Path tempFile;

    @BeforeEach
    public void setUp() {
        dataProcessor = new TemperatureDataProcessor();
        try {
            tempFile = Files.createTempFile("test_data", ".csv");
            String testData = "CityA;2023-01-01;25.0\nCityA;2023-02-01;28.0\nCityB;2023-01-01;20.0\nCityB;2023-02-01;22.0";
            Files.write(tempFile, testData.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temporary file.", e);
        }
    }

    @Test
    public void shouldProcessTemperatureDataWhenFileExistsAndContainsDataForCity() {
        String city = "CityA";

        List<YearlyAverageTemperature> yearlyAverages = dataProcessor.processTemperatureData(tempFile, city);

        assertFalse(yearlyAverages.isEmpty());
        assertEquals(1, yearlyAverages.size());
        assertEquals("2023", yearlyAverages.get(0).getYear());
        assertEquals(26.5, yearlyAverages.get(0).getAverageTemperature(), 0.01);
    }

    @Test
    public void shouldProcessTemperatureDataWhenFileDoesNotExist() {
        String city = "CityA";
        Path nonExistentFile = Paths.get("non_existent_file.csv");

        List<YearlyAverageTemperature> yearlyAverages = dataProcessor.processTemperatureData(nonExistentFile, city);

        assertTrue(yearlyAverages.isEmpty());
    }

    @Test
    public void shouldProcessTemperatureDataWhenFileContainsInvalidData() {
        String city = "CityA";
        try {
            Path tempFileInvalid = Files.createTempFile("invalid_data", ".csv");
            String invalidData = "CityA;2023-01-01;25.0\nCityB;invalid_date;28.0\nCityA;2023-02-01;22.0";
            Files.write(tempFileInvalid, invalidData.getBytes());

            List<YearlyAverageTemperature> yearlyAverages = dataProcessor.processTemperatureData(tempFileInvalid, city);

            assertFalse(yearlyAverages.isEmpty());
            assertEquals(1, yearlyAverages.size());
            assertEquals("2023", yearlyAverages.get(0).getYear());
            assertEquals(23.5, yearlyAverages.get(0).getAverageTemperature(), 0.01);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temporary file with invalid data.", e);
        }
    }

    @Test
    public void shouldProcessTemperatureDataWhenFileIsEmpty() {
        String city = "CityA";
        try {
            Path tempFileEmpty = Files.createTempFile("empty_file", ".csv");

            List<YearlyAverageTemperature> yearlyAverages = dataProcessor.processTemperatureData(tempFileEmpty, city);

            assertTrue(yearlyAverages.isEmpty());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temporary empty file.", e);
        }
    }
}
