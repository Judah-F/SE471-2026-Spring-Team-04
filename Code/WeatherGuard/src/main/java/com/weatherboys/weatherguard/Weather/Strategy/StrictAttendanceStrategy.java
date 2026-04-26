package com.weatherboys.weatherguard.Weather.Strategy;
import com.weatherboys.weatherguard.Weather.Weather;

import java.time.LocalDateTime;

public class StrictAttendanceStrategy implements AttendanceRuleStrategyIF {
    public String STATUS_PRESENT="";
    public String STATUS_LATE="";
    public String STATUS_ABSENT="";

    public String determineStatus(LocalDateTime checkInTime, LocalDateTime sessionStart, Weather weather){


        if(checkInTime.isAfter(sessionStart) ){ //if student does not check in precicly when they should have they are late
            return STATUS_LATE;
        }
        else if(checkInTime.equals(sessionStart)){
            return STATUS_PRESENT;
        }
        else
            return STATUS_ABSENT;
    }
    public String name(){
        return "";
    }

}
