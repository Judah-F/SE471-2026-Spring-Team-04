package com.weatherboys.weatherguard.Strategy;

import com.weatherboys.weatherguard.Weather.Weather;
import java.time.LocalDateTime;
import java.util.logging.Logger;

public class StrictAttendanceStrategy implements AttendanceRuleStrategyIF {

    private static final Logger logger = Logger.getLogger(StrictAttendanceStrategy.class.getName());

    @Override
    public String determineStatus(LocalDateTime checkInTime, LocalDateTime sessionStart, Weather weather){

        if(checkInTime == null || sessionStart == null) {
            logger.warning("Strict determineStatus: null timestamp — returning ABSENT");
            return STATUS_ABSENT;
        }
        if(!checkInTime.isAfter(sessionStart.plusSeconds(120))){
            return STATUS_PRESENT;
        }
        return STATUS_ABSENT;
    }

    @Override
    public String name(){
        return "Strict";
    }

}
