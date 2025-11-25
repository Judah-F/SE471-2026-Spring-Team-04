package com.weatherboys.model;

/**
 * Student - Transfer Object (DAO Pattern)
 *
 * Represents a student enrolled in a class.
 * Used to transfer student data between layers (Controller, DatabaseManager).
 *
 * Design Pattern: Transfer Object (from Lab10 DAO Pattern)
 */
public class Student {
    private String studentId;
    private String studentName;
    private String classId;

    /**
     * Default constructor
     */
    public Student() {
    }

    /**
     * Constructor with all fields
     *
     * @param studentId Student's unique ID (e.g., S001)
     * @param studentName Student's full name
     * @param classId Class ID this student is enrolled in
     */
    public Student(String studentId, String studentName, String classId) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.classId = classId;
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

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Override
    public String toString() {
        return studentName + " (" + studentId + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return studentId.equals(student.studentId);
    }

    @Override
    public int hashCode() {
        return studentId.hashCode();
    }
}
