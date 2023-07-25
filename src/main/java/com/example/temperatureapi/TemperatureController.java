package com.example.temperatureapi;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TemperatureController {

    @GetMapping("/averageTemperatures")
    public List<YearlyAverageTemperature> getAverageTemperatures(@RequestParam String city) {
        TemperatureDataProcessor dataProcessor = new TemperatureDataProcessor(city);
        try {
            Resource resource = new ClassPathResource("example_file.csv");
            Path filePath = resource.getFile().toPath();
            return dataProcessor.calculateYearlyAverageTemperatures(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
