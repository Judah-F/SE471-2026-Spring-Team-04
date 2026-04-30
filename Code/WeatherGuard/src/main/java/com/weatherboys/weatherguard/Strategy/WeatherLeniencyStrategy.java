package com.weatherboys.weatherguard.Strategy;

import com.weatherboys.weatherguard.Weather.Weather;
import java.time.LocalDateTime;


public class WeatherLeniencyStrategy implements AttendanceRuleStrategyIF {

    private final AttendanceRuleStrategyIF inner;
    private final int extraGraceMinutes;

    /** Default: 2 extra grace minutes when weather is extreme. */
    public WeatherLeniencyStrategy(AttendanceRuleStrategyIF inner) {
        this(inner, 2);
    }

    public WeatherLeniencyStrategy(AttendanceRuleStrategyIF inner, int extraGraceMinutes) {
        if (inner == null) throw new IllegalArgumentException("inner strategy is null");
        if (extraGraceMinutes < 0) throw new IllegalArgumentException("extraGraceMinutes must be non-negative");
        this.inner = inner;
        this.extraGraceMinutes = extraGraceMinutes;
    }

    @Override
    public String determineStatus(LocalDateTime checkInTime, LocalDateTime sessionStart, Weather weather) {
        String innerResult = inner.determineStatus(checkInTime, sessionStart, weather);

        if (!isExtreme(weather)) return innerResult;

        if (!STATUS_LATE.equals(innerResult)) return innerResult;

        if (checkInTime == null || sessionStart == null) return innerResult;
        LocalDateTime shifted = checkInTime.minusMinutes(extraGraceMinutes);
        String shiftedResult = inner.determineStatus(shifted, sessionStart, weather);

        return STATUS_PRESENT.equals(shiftedResult) ? STATUS_PRESENT : innerResult;
    }

    private boolean isExtreme(Weather weather) {
        if (weather == null || weather.isEmpty()) return false;
        return weather.getCurrentTemp() < 32;
    }

    @Override
    public String name() {
        return "WeatherLenient(+" + extraGraceMinutes + "min in cold, on " + inner.name() + ")";
    }
}