package com.example.temperatureapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class TemperatureController {
    private final String FILE_PATH = "example_file.csv";

    @GetMapping("/averageTemperatures")
    public List<YearlyAverageTemperature> getAverageTemperatures(@RequestParam String city) throws IOException {
        TemperatureDataProcessor dataProcessor = new TemperatureDataProcessor();
        Resource resource = new ClassPathResource(FILE_PATH);
        Path filePath = Paths.get(resource.getURI());

        return dataProcessor.processTemperatureData(filePath, city);
    }

    @ExceptionHandler(IOException.class)
    public List<YearlyAverageTemperature> handleIOException(IOException e) {
        log.error("An error occurred while processing the request: ", e);
        return Collections.emptyList();
    }
}
