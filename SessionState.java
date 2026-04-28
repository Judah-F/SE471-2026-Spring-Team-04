package com.weatherboys.state;

import com.weatherboys.ui.TeacherViewController;

public abstract class SessionState {

    public abstract void startSession(TeacherViewController ctx);

    public abstract void endSession(TeacherViewController ctx);

    public abstract void handleCheckIn(TeacherViewController ctx, String studentId);

    public abstract String name();

    protected void onEnter(TeacherViewController ctx) {
        // default
    }

    protected void onExit(TeacherViewController ctx) {
        // default
    }
}
