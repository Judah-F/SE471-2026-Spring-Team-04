package com.weatherboys.observer;

import com.weatherboys.model.AttendanceEvent;

public interface SessionObservableIF {
    void attach(AttendanceObserverIF observer);
    void detach(AttendanceObserverIF observer);
    void notifyObservers(AttendanceEvent event);
}
