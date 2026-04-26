package com.weatherboys.weatherguard.Weather.Strategy;
import com.weatherboys.weatherguard.Weather.Weather;

import java.time.LocalDateTime;

public class WeatherLeniencyStrategy implements AttendanceRuleStrategyIF {
    public String STATUS_PRESENT="";
    public String STATUS_LATE="";
    public String STATUS_ABSENT="";
    int extraMinutes;
    private AttendanceRuleStrategyIF inner;
    public String determineStatus(LocalDateTime checkInTime, LocalDateTime sessionStart, Weather weather){

       //extraMinutes =  //need to add weather logic for how much the wind affects how much extra time the student should have
        LocalDateTime Grace = sessionStart.plusMinutes(extraMinutes); //Grace period
        if (checkInTime.isAfter(Grace)) {
            return STATUS_LATE;
        } else if (checkInTime.isBefore(Grace)) {
            return STATUS_PRESENT;
        } else {
            return STATUS_ABSENT;
        }


    }

    public String name(){
        return "";
    }
}
