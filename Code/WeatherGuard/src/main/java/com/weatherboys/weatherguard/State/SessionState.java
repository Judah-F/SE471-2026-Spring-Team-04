package com.weatherboys.weatherguard.State;

import com.weatherboys.ui.TeacherViewController;

public abstract class SessionState {

    public abstract void startSession(TeacherViewController ctx);

    public abstract void endSession(TeacherViewController ctx);

    public abstract void handleCheckIn(TeacherViewController ctx, String studentId);

    public abstract String name();

    public void onEnter(TeacherViewController ctx) {
        // default
    }

    public void onExit(TeacherViewController ctx) {
        // default
    }
}
