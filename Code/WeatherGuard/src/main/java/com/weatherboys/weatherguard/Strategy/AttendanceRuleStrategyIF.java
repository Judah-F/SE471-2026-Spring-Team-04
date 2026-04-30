package com.weatherboys.weatherguard.Strategy;

import com.weatherboys.weatherguard.Weather.Weather;
import java.time.LocalDateTime;

public interface AttendanceRuleStrategyIF {
    public String STATUS_PRESENT="present";
    public String STATUS_LATE="late";
    public String STATUS_ABSENT="absent";

    public String determineStatus(LocalDateTime checkInTime, LocalDateTime sessionStart, Weather weather);
    public String name();
    default int getEffectivePresentMinutes(Weather weather) { return 0; }
    default int getWeatherBonusMinutes(Weather weather) { return 0; }
}
