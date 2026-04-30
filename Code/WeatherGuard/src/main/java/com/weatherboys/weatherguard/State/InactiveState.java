package com.weatherboys.weatherguard.State;

import com.weatherboys.ui.TeacherViewController;

import java.time.LocalDateTime;
import java.util.logging.Logger;

public class InactiveState extends SessionState {

    private static final Logger logger = Logger.getLogger(InactiveState.class.getName());

    @Override
    public void startSession(TeacherViewController ctx) {
        if (!ctx.validateRoster()) {
            return;
        }
        ctx.setState(new ActiveState(LocalDateTime.now(), ctx.getAttendanceRule()));
    }

    @Override
    public void endSession(TeacherViewController ctx) {
        // Nothing is running.
    }

    @Override
    public void handleCheckIn(TeacherViewController ctx, String studentId) {
        logger.warning("Check-in dropped: no active session for " + studentId);
    }

    @Override
    public String name() { return "Inactive"; }
}
