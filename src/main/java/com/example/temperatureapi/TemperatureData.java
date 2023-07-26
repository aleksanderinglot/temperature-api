package com.example.temperatureapi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TemperatureData {
    private final String cityName;
    private final String dateStr;
    private final double temperature;
}
