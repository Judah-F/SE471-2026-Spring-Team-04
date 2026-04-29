package com.weatherboys.weatherguard.Observer;

import java.time.LocalDateTime;
import java.util.Objects;

public final class AttendanceEvent {

    public enum Kind {
        CHECK_IN,
        STATUS_CHANGED,
        SESSION_CLOSED
    }

    private final Kind kind;
    private final String sessionId;
    private final String classId;
    private final String studentId;
    private final String studentName;
    private final String status; // "present" / "late" / "absent" — set by the Strategy pattern
    private final LocalDateTime timestamp;

    public AttendanceEvent(Kind kind, String sessionId, String classId, String studentId, String studentName, String status, LocalDateTime timestamp) {
        if (kind == null) throw new IllegalArgumentException("kind is null");
        if (sessionId == null) throw new IllegalArgumentException("sessionId is null");
        if (classId == null) throw new IllegalArgumentException("classId is null");
        if (timestamp == null) throw new IllegalArgumentException("timestamp is null");

        if (kind != Kind.SESSION_CLOSED && studentId == null) {
            System.err.println("AttendanceEvent: " + kind + " event created with null studentId");
        }

        this.kind = kind;
        this.sessionId = sessionId;
        this.classId = classId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.status = status;
        this.timestamp = timestamp;
    }

    public Kind getKind() { return kind; }
    public String getSessionId() { return sessionId; }
    public String getClassId() { return classId; }
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public String getAttendanceId() {
        return kind + ":" + sessionId + ":" + studentId + ":" + timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttendanceEvent)) return false;
        AttendanceEvent that = (AttendanceEvent) o;
        return kind == that.kind && Objects.equals(sessionId, that.sessionId) && Objects.equals(studentId, that.studentId) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, sessionId, studentId, timestamp);
    }

    @Override
    public String toString() {
        return "AttendanceEvent{" + getAttendanceId() + ", status=" + status + "}";
    }
}
