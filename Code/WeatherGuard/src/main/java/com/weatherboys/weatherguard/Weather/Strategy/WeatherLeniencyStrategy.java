package com.weatherboys.weatherguard.Weather.Strategy;
import com.weatherboys.weatherguard.Weather.Weather;

import java.time.LocalDateTime;

public class WeatherLeniencyStrategy implements AttendanceRuleStrategyIF {
    public String STATUS_PRESENT="";
    public String STATUS_LATE="";
    public String STATUS_ABSENT="";

    public String determineStatus(LocalDateTime checkInTime, LocalDateTime sessionStart, Weather weather){
        return "";
    }

    public String name(){
        return "";
    }
}
