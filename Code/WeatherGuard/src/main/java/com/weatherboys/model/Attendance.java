package com.weatherboys.model;

import java.time.LocalDateTime;

/**
 * Attendance - Transfer Object (DAO Pattern)
 *
 * Represents a student's attendance record for a session.
 * Used to transfer attendance data between layers (Controller, DatabaseManager).
 *
 * Design Pattern: Transfer Object (from Lab10 DAO Pattern)
 */
public class Attendance {
    private String studentId;
    private String studentName;
    private LocalDateTime checkInTime;
    private String sessionId;
    private String classId;
    private String status;

    /**
     * Default constructor
     */
    public Attendance() {
    }

    /**
     * Constructor with all fields
     *
     * @param studentId Student's unique ID
     * @param studentName Student's full name
     * @param checkInTime Timestamp when student checked in
     * @param sessionId Session identifier
     * @param classId Class identifier
     * @param status Attendance status (e.g., "present", "late", "absent")
     */
    public Attendance(String studentId, String studentName, LocalDateTime checkInTime,
                      String sessionId, String classId, String status) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.checkInTime = checkInTime;
        this.sessionId = sessionId;
        this.classId = classId;
        this.status = status;
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "studentId='" + studentId + '\'' +
                ", studentName='" + studentName + '\'' +
                ", checkInTime=" + checkInTime +
                ", sessionId='" + sessionId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Attendance that = (Attendance) obj;
        return studentId.equals(that.studentId) && sessionId.equals(that.sessionId);
    }

    @Override
    public int hashCode() {
        return (studentId + sessionId).hashCode();
    }
}
