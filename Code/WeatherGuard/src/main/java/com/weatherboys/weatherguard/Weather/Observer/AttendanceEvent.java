package com.weatherboys.weatherguard.Weather.Observer;

import java.time.Instant;

public class AttendanceEvent {
    private Kind kind;
    private String sessionId;
    private String classId;
    private String studentId;
    private String StudentName;
    private String status;
    private Instant timestamp;

    public Kind getKind(){
        return this.kind;
    }
   public String getSessionId(){
        return this.sessionId;
   }
    public String getStudentId(){
        return this.studentId;
    }
    public String getStatus(){
        return this.status;
    }


}
