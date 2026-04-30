package com.weatherboys.weatherguard.State;

import com.weatherboys.ui.TeacherViewController;
import com.weatherboys.weatherguard.Strategy.AttendanceRuleStrategyIF;
import com.weatherboys.weatherguard.Weather.Weather;

import java.time.LocalDateTime;
import java.util.logging.Logger;

public class ActiveState extends SessionState {

    private static final Logger logger = Logger.getLogger(ActiveState.class.getName());

    private final LocalDateTime startedAt;
    private final AttendanceRuleStrategyIF rule;

    public ActiveState(LocalDateTime startedAt, AttendanceRuleStrategyIF rule) {
        this.startedAt = startedAt;
        this.rule = rule;
    }

    public LocalDateTime getStartedAt() { return startedAt; }

    @Override
    public void startSession(TeacherViewController ctx) {
        logger.info("startSession ignored — already Active.");
    }

    @Override
    public void endSession(TeacherViewController ctx) {
        ctx.setState(new ClosedState());
    }

    @Override
    public void handleCheckIn(TeacherViewController ctx, String studentId) {
        LocalDateTime checkInTime = LocalDateTime.now();
        Weather weather = ctx.getCurrentWeather();
        String status = rule.determineStatus(checkInTime, startedAt, weather);
        System.out.println("[AttendanceRule] " + rule.name() + ": " + studentId + " -> " + status);
        ctx.recordCheckIn(studentId, status);
    }

    @Override
    public void onEnter(TeacherViewController ctx) {
        ctx.openSessionUI();
        ctx.startPolling();
        ctx.startLabelRefresh(this.startedAt);
    }

    @Override
    public void onExit(TeacherViewController ctx) {
        ctx.stopPolling();
        ctx.stopLabelRefresh();
    }

    @Override
    public String name() { return "Active"; }
}
