package com.weatherboys.weatherguard.State;

import com.weatherboys.ui.TeacherViewController;

import java.util.logging.Logger;

public class InactiveState extends SessionState {

    private static final Logger logger = Logger.getLogger(InactiveState.class.getName());

    @Override
    public void startSession(TeacherViewController ctx) {

        boolean opened = ctx.openSessionPlumbing();
        if (!opened) {
            // Failed to open (e.g., no roster, config error). Stay Inactive.
            return;
        }

        ctx.setState(new ActiveState());
    }

    @Override
    public void endSession(TeacherViewController ctx) {
        // Nothing is running.
    }

    @Override
    public void handleCheckIn(TeacherViewController ctx, String studentId) {
        logger.warning("Check-in received for " + studentId + " but no active session; dropping.");
    }

    @Override
    public String name() {
        return "Inactive";
    }
}
