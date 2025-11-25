package com.weatherboys.model;

import java.time.LocalDateTime;

/**
 * Session - Transfer Object (DAO Pattern)
 *
 * Represents an attendance session for a class.
 * Used to transfer session data between layers (Controller, DatabaseManager).
 *
 * Design Pattern: Transfer Object (from Lab10 DAO Pattern)
 */
public class Session {
    private String classId;
    private String sessionId;
    private LocalDateTime createdAt;
    private String weatherData;
    private boolean active;

    /**
     * Default constructor
     */
    public Session() {
    }

    /**
     * Constructor with all fields
     *
     * @param classId Class identifier (e.g., BIO101)
     * @param sessionId Unique session identifier
     * @param createdAt Timestamp when session was created
     * @param weatherData JSON string of weather data at session start
     * @param active Whether the session is currently active
     */
    public Session(String classId, String sessionId, LocalDateTime createdAt, String weatherData, boolean active) {
        this.classId = classId;
        this.sessionId = sessionId;
        this.createdAt = createdAt;
        this.weatherData = weatherData;
        this.active = active;
    }

    // Getters and Setters
    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(String weatherData) {
        this.weatherData = weatherData;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Session{" +
                "classId='" + classId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", createdAt=" + createdAt +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Session session = (Session) obj;
        return sessionId.equals(session.sessionId);
    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }
}
