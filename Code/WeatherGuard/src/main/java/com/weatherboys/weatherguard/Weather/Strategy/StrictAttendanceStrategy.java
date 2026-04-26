package com.weatherboys.weatherguard.Weather.Strategy;
import com.weatherboys.weatherguard.Weather.Weather;
public class StrictAttendanceStrategy implements AttendanceRuleStrategyIF {
    public String STATUS_PRESENT="";
    public String STATUS_LATE="";
    public String STATUS_ABSENT="";

    public String determineStatus(int checkin, int startTime, Weather weather){
        return "";
    }
    public String name(){
        return "";
    }

}
