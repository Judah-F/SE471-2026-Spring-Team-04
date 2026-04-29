package com.weatherboys.weatherguard.Strategy;

import com.weatherboys.weatherguard.Weather.Weather;
import java.time.LocalDateTime;

public class WeatherLeniencyStrategy implements AttendanceRuleStrategyIF {

    private final AttendanceRuleStrategyIF inner;
    private final int extremeLenienceMinutes;

    // Default: 10-minute extended grace window when weather is extreme.
    public WeatherLeniencyStrategy(AttendanceRuleStrategyIF inner) {
        this(inner, 10);
    }

    public WeatherLeniencyStrategy(AttendanceRuleStrategyIF inner, int extremeLenienceMinutes) {
        if (inner == null) throw new IllegalArgumentException("inner strategy is null");
        if (extremeLenienceMinutes < 0) throw new IllegalArgumentException("extremeLenienceMinutes must be non-negative");
        this.inner = inner;
        this.extremeLenienceMinutes = extremeLenienceMinutes;
    }

    @Override
    public String determineStatus(LocalDateTime checkInTime, LocalDateTime sessionStart, Weather weather){
        if (isExtreme(weather) && checkInTime != null && sessionStart != null && !checkInTime.isAfter(sessionStart.plusMinutes(extremeLenienceMinutes))) {
            return STATUS_PRESENT;
        }
        // Normal weather (or outside the extended window)
        return inner.determineStatus(checkInTime, sessionStart, weather);
    }

    private boolean isExtreme(Weather weather) {
        if (weather == null || weather.isEmpty()) return false;
        int tempF = weather.getCurrentTemp();
        return tempF < 32;
    }

    @Override
    public String name(){
        return "WeatherLenient(" + inner.name() + ")";
    }
}
