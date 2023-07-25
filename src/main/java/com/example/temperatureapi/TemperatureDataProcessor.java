package com.example.temperatureapi;

import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class TemperatureDataProcessor {
    private final String city;

    public List<YearlyAverageTemperature> calculateYearlyAverageTemperatures(Path filePath) {
        Map<String, Double[]> yearlyTemperatureData = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String cityName = parts[0];
                String dateStr = parts[1];
                double temperature = Double.parseDouble(parts[2]);

                if (cityName.equals(city)) {
                    String year = dateStr.substring(0, 4);
                    Double[] sumAndCount = yearlyTemperatureData.getOrDefault(year, new Double[]{0.0, 0.0});
                    sumAndCount[0] += temperature;
                    sumAndCount[1]++;
                    yearlyTemperatureData.put(year, sumAndCount);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<YearlyAverageTemperature> yearlyAverages = new ArrayList<>();
        for (Map.Entry<String, Double[]> entry : yearlyTemperatureData.entrySet()) {
            String year = entry.getKey();
            Double[] sumAndCount = entry.getValue();
            double averageTemperature = sumAndCount[0] / sumAndCount[1];
            yearlyAverages.add(new YearlyAverageTemperature(year, averageTemperature));
        }

        return yearlyAverages;
    }
}
