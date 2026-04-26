package com.weatherboys.weatherguard.Weather.Strategy;

import com.weatherboys.weatherguard.Weather.Weather;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;

public class GracePeriodStrategy implements AttendanceRuleStrategyIF {
    public String STATUS_PRESENT="";
    public String STATUS_LATE="";
    public String STATUS_ABSENT="";

    private int presentMinutes;
    private int lateWindowMinutes;


    public String determineStatus(LocalDateTime checkInTime, LocalDateTime sessionStart, Weather weather) {

        LocalDateTime Grace = sessionStart.plusMinutes(presentMinutes); //Grace period 

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
