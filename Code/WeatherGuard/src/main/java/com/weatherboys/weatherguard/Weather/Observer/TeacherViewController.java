package com.weatherboys.weatherguard.Weather.Observer;

import java.util.concurrent.ScheduledExecutorService;

public class TeacherViewController implements AttendanceObserverIF {
    private ScheduledExecutorService netlifyPoller;

    public void onAttendanceChanged(AttendanceEvent attendanceEvent){

    }

    private void applyStatusToUi(String studentId, String status) {

    }
}
