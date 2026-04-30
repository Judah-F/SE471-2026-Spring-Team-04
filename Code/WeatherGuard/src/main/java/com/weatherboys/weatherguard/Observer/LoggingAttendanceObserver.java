package com.weatherboys.weatherguard.Observer;

public class LoggingAttendanceObserver implements AttendanceObserverIF {

    @Override
    public void onAttendanceChanged(AttendanceEvent event) {
        StringBuilder sb = new StringBuilder("[Observer] ");
        sb.append(event.getKind());
        sb.append(" sessionId=").append(event.getSessionId());
        sb.append(" classId=").append(event.getClassId());
        if (event.getStudentId() != null) {
            sb.append(" studentId=").append(event.getStudentId());
        }
        if (event.getStatus() != null) {
            sb.append(" status=").append(event.getStatus());
        }
        sb.append(" ts=").append(event.getTimestamp());
        System.out.println(sb.toString());
    }
}
