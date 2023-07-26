package com.example.temperatureapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
public class TemperatureDataProcessor {
    private final ConcurrentHashMap<String, DoubleSummaryStatistics> yearlyTemperatureData = new ConcurrentHashMap<>();

    public List<YearlyAverageTemperature> processTemperatureData(Path filePath, String city) {
        yearlyTemperatureData.clear();
        List<TemperatureData> dataList = readTemperatureData(filePath, city);
        return calculateYearlyAverageTemperatures(dataList);
    }

    private List<TemperatureData> readTemperatureData(Path filePath, String city) {
        List<TemperatureData> dataList = new ArrayList<>();

        try (Stream<String> lines = Files.lines(filePath)) {
            dataList = lines
                    .map(line -> line.split(";"))
                    .filter(parts -> parts.length >= 3)
                    .filter(parts -> parts[0].equals(city))
                    .map(parts -> {
                        String cityName = parts[0];
                        String dateStr = parts[1];
                        double temperature = Double.parseDouble(parts[2]);
                        return new TemperatureData(cityName, dateStr, temperature);
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("An error occurred while reading temperature data from file: {}", filePath, e);
        }

        return dataList;
    }

    private List<YearlyAverageTemperature> calculateYearlyAverageTemperatures(List<TemperatureData> dataList) {
        dataList.parallelStream().forEach(data -> {
            String year = data.getDateStr().substring(0, 4);
            double temperature = data.getTemperature();
            yearlyTemperatureData.computeIfAbsent(year, k -> new DoubleSummaryStatistics()).accept(temperature);
        });

        List<YearlyAverageTemperature> yearlyAverages = yearlyTemperatureData.entrySet().parallelStream()
                .map(entry -> {
                    String year = entry.getKey();
                    DoubleSummaryStatistics stats = entry.getValue();
                    double averageTemperature = stats.getAverage();
                    return new YearlyAverageTemperature(year, averageTemperature);
                })
                .collect(Collectors.toList());

        return yearlyAverages;
    }
}
