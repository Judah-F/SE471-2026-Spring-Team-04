package com.weatherboys.state;

import com.weatherboys.ui.TeacherViewController;

import java.util.logging.Logger;

public class ActiveState extends SessionState {

    private static final Logger logger = Logger.getLogger(ActiveState.class.getName());

    @Override
    public void startSession(TeacherViewController ctx) {
        logger.info("startSession called while already Active — ignoring.");
    }

    @Override
    public void endSession(TeacherViewController ctx) {
        ctx.closeSessionPlumbing();
        ctx.setState(new ClosedState());
    }

    @Override
    public void handleCheckIn(TeacherViewController ctx, String studentId) {
        ctx.recordCheckIn(studentId);
    }

    @Override
    public String name() {
        return "Active";
    }
}
