package com.weatherboys.weatherguard.Weather.Observer;

public interface SessionObservableIF {
    public void attach(AttendanceObserverIF attendanceObserverIF);
    public void deatach(AttendanceObserverIF attendanceObserverIF);
    public void notifyObservers(AttendanceEvent attendanceEvent);


}
