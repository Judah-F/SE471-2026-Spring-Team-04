package com.weatherboys.weatherguard.Observer;

public interface SessionObservableIF {

    void attach(AttendanceObserverIF observer);

    void detach(AttendanceObserverIF observer);

    void notifyObservers(AttendanceEvent event);
}
