package com.weatherboys.weatherguard.Weather.Strategy;

public class GracePeriodStrategy implements AttendanceRuleStrategyIF {
    public String STATUS_PRESENT="";
    public String STATUS_LATE="";
    public String STATUS_ABSENT="";

    private int presentMinutes;
    private int lateWindowMinutes;


    public String determineStatus(){
        return "";
    }
    public String name(){
        return "";
    }

}
