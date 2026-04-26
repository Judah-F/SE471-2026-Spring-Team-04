package com.weatherboys.weatherguard.Weather.Strategy;

import com.weatherboys.weatherguard.Weather.Weather;

public interface AttendanceRuleStrategyIF {
    public String STATUS_PRESENT="";
    public String STATUS_LATE="";
    public String STATUS_ABSENT="";

    public String determineStatus(int checkIn, int sessionStart, Weather weather);
    public String name();
}
