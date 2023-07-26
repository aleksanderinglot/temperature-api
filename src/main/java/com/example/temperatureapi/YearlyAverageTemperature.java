package com.example.temperatureapi;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class YearlyAverageTemperature {
    private String year;
    private double averageTemperature;
}
