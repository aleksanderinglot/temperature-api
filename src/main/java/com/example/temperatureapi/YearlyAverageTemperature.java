package com.example.temperatureapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class YearlyAverageTemperature {
    private String year;
    private double averageTemperature;
}
