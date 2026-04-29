package com.weatherboys.weatherguard.State;

import com.weatherboys.ui.TeacherViewController;

import java.util.logging.Logger;

public class ClosedState extends SessionState {

    private static final Logger logger = Logger.getLogger(ClosedState.class.getName());

    @Override
    public void startSession(TeacherViewController ctx) {
        // Allow restart: bounce through Inactive to reach Active.
        ctx.setState(new InactiveState());
        ctx.getState().startSession(ctx);
    }

    @Override
    public void endSession(TeacherViewController ctx) {
        // Already closed.
    }

    @Override
    public void handleCheckIn(TeacherViewController ctx, String studentId) {
        logger.info("Late check-in dropped: " + studentId + " arrived after session closed.");
    }

    @Override
    public void onEnter(TeacherViewController ctx) {
        ctx.closeSessionUI();   // closes DB, updates pie chart, paints gray, switches buttons
    }

    @Override
    public String name() { return "Closed"; }
}
