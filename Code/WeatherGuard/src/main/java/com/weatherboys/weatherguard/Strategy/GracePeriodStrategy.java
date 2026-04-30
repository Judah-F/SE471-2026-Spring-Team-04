package com.weatherboys.weatherguard.Strategy;

import com.weatherboys.weatherguard.Weather.Weather;
import java.util.logging.Logger;
import java.time.LocalDateTime;

public class GracePeriodStrategy implements AttendanceRuleStrategyIF {

    private static final Logger logger = Logger.getLogger(GracePeriodStrategy.class.getName());
    private final int presentMinutes;
    private final int lateWindowMinutes;

    public GracePeriodStrategy() {
        this(1, 10);
    }

    public GracePeriodStrategy(int presentMinutes, int lateWindowMinutes) {
        if (presentMinutes < 0 || lateWindowMinutes < 0) {
            throw new IllegalArgumentException("GracePeriodStrategy: must be non-negative");
        }
        this.presentMinutes = presentMinutes;
        this.lateWindowMinutes = lateWindowMinutes;
    }

    @Override
    public String determineStatus(LocalDateTime checkInTime, LocalDateTime sessionStart, Weather weather) {

        if (checkInTime == null || sessionStart == null) {
            logger.warning("determineStatus: null timestamp — returning ABSENT");
            return STATUS_ABSENT;
        }

        LocalDateTime presentCutoff = sessionStart.plusMinutes(presentMinutes);
        LocalDateTime lateCutoff    = presentCutoff.plusMinutes(lateWindowMinutes);

        if (!checkInTime.isAfter(presentCutoff)) return STATUS_PRESENT;
        if (!checkInTime.isAfter(lateCutoff))    return STATUS_LATE;
        return STATUS_ABSENT;
    }

    @Override
    public int getEffectivePresentMinutes(com.weatherboys.weatherguard.Weather.Weather weather) {
        return presentMinutes;
    }

    @Override
    public String name(){
        return "Grace(" + presentMinutes + "/" + lateWindowMinutes + ")";
    }

}
