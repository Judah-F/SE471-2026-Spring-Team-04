package com.weatherboys.model;

import java.time.Instant;

public final class AttendanceEvent {
    public enum Kind {
        Check_IN,
        Status_Changed,
        Session_Closed
    }

    private final Kind kind;
    private final String sessionId;
    private final String classId;
    private final String studentId;
    private final String studentName;
    private final String status;
    private final Instant timestamp;

    public AttendanceEvent(Kind kind, String sessionId, String classID, String studentID, String studentName, String status) {
        this.kind = kind;
        this.sessionId = sessionId;
        this.classId = classID;
        this.studentId = studentID;
        this.studentName = studentName;
        this.status = status;
        this.timestamp = Instant.now();

    }
    public Kind getKind(){return kind;}
    public String getSessionId(){return sessionId;}
    public String getClassId(){return classId;}
    public String getStudentId(){return studentId;}
    public String getStudentName(){return studentName;}
    public String getStatus(){return status;}
    public Instant gettimestamp(){return timestamp;}




}
