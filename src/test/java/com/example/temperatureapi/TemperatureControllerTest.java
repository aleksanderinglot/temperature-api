package com.example.temperatureapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TemperatureControllerTest {

    @Mock
    private TemperatureDataProcessor dataProcessor;

    @InjectMocks
    private TemperatureController controller;

    private Path tempFile;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new TemperatureController();

        try {
            Resource resource = new ClassPathResource("example_file.csv");
            tempFile = Paths.get(resource.getURI());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temporary file.", e);
        }
    }

    @Test
    public void shouldReturnAverageTemperaturesForExistingCity() throws IOException {
        String city = "CityA";
        List<YearlyAverageTemperature> expectedResults = Collections.singletonList(new YearlyAverageTemperature("2023", 26.5));
        when(dataProcessor.processTemperatureData(any(Path.class), any(String.class))).thenReturn(expectedResults);

        List<YearlyAverageTemperature> yearlyAverages = controller.getAverageTemperatures(city);

        assertEquals(expectedResults, yearlyAverages);
    }

    @Test
    public void shouldReturnEmptyListForNonExistentCity() throws IOException {
        String city = "NonExistentCity";
        List<YearlyAverageTemperature> expectedResults = Collections.emptyList();
        when(dataProcessor.processTemperatureData(any(Path.class), any(String.class))).thenReturn(expectedResults);

        List<YearlyAverageTemperature> yearlyAverages = controller.getAverageTemperatures(city);

        assertTrue(yearlyAverages.isEmpty());
    }
}
