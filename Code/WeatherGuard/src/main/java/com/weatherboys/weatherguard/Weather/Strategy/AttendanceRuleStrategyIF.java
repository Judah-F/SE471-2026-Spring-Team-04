package com.weatherboys.weatherguard.Weather.Strategy;

public interface AttendanceRuleStrategyIF {
    public String STATUS_PRESENT="";
    public String STATUS_LATE="";
    public String STATUS_ABSENT="";

    public String determineStatus(int checkIn, int sessionStart, int weather);
    public String name();
}
