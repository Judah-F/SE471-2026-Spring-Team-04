package com.weatherboys.weatherguard.Weather.Strategy;

import com.weatherboys.weatherguard.Weather.Weather;

public class GracePeriodStrategy implements AttendanceRuleStrategyIF {
    public String STATUS_PRESENT="";
    public String STATUS_LATE="";
    public String STATUS_ABSENT="";

    private int presentMinutes;
    private int lateWindowMinutes;


    public String determineStatus(int checkin, int sessionStart, Weather weather){
        return "";
    }
    public String name(){
        return "";
    }

}
