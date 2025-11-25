package com.weatherboys.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * ClassInfo - Transfer Object (DAO Pattern)
 *
 * Represents a class with its metadata.
 * Used to transfer class data between layers (Controller, DatabaseManager).
 * Includes JavaFX Properties for TableView binding.
 *
 * Design Pattern: Transfer Object (from Lab10 DAO Pattern)
 */
public class ClassInfo {
    // JavaFX Properties for TableView binding
    private final StringProperty classId;
    private final StringProperty className;
    private final StringProperty semester;
    private final IntegerProperty year;
    private final StringProperty professorName;
    private final StringProperty city;
    private final StringProperty startDate;
    private final StringProperty endDate;

    /**
     * Default constructor
     */
    public ClassInfo() {
        this.classId = new SimpleStringProperty("");
        this.className = new SimpleStringProperty("");
        this.semester = new SimpleStringProperty("");
        this.year = new SimpleIntegerProperty(0);
        this.professorName = new SimpleStringProperty("");
        this.city = new SimpleStringProperty("");
        this.startDate = new SimpleStringProperty("");
        this.endDate = new SimpleStringProperty("");
    }

    /**
     * Constructor with all fields
     *
     * @param classId Class ID (e.g., BIO101)
     * @param className Full class name (e.g., Biology 101)
     * @param semester Semester (e.g., Fall)
     * @param year Year (e.g., 2025)
     * @param professorName Professor's name
     * @param city City location for weather data
     * @param startDate Class start date
     * @param endDate Class end date
     */
    public ClassInfo(String classId, String className, String semester, int year,
                     String professorName, String city, String startDate, String endDate) {
        this.classId = new SimpleStringProperty(classId);
        this.className = new SimpleStringProperty(className);
        this.semester = new SimpleStringProperty(semester);
        this.year = new SimpleIntegerProperty(year);
        this.professorName = new SimpleStringProperty(professorName);
        this.city = new SimpleStringProperty(city);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
    }

    // Property getters (for JavaFX binding)
    public StringProperty classIdProperty() {
        return classId;
    }

    public StringProperty classNameProperty() {
        return className;
    }

    public StringProperty semesterProperty() {
        return semester;
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    public StringProperty professorNameProperty() {
        return professorName;
    }

    public StringProperty cityProperty() {
        return city;
    }

    public StringProperty startDateProperty() {
        return startDate;
    }

    public StringProperty endDateProperty() {
        return endDate;
    }

    // Standard getters (for regular access)
    public String getClassId() {
        return classId.get();
    }

    public String getClassName() {
        return className.get();
    }

    public String getSemester() {
        return semester.get();
    }

    public int getYear() {
        return year.get();
    }

    public String getProfessorName() {
        return professorName.get();
    }

    public String getCity() {
        return city.get();
    }

    public String getStartDate() {
        return startDate.get();
    }

    public String getEndDate() {
        return endDate.get();
    }

    // Standard setters
    public void setClassId(String classId) {
        this.classId.set(classId);
    }

    public void setClassName(String className) {
        this.className.set(className);
    }

    public void setSemester(String semester) {
        this.semester.set(semester);
    }

    public void setYear(int year) {
        this.year.set(year);
    }

    public void setProfessorName(String professorName) {
        this.professorName.set(professorName);
    }

    public void setCity(String city) {
        this.city.set(city);
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
    }

    /**
     * Gets a formatted display string for semester and year
     * Example: "Fall 2025"
     */
    public String getFormattedSemester() {
        return semester.get() + " " + year.get();
    }

    @Override
    public String toString() {
        return className.get() + " (" + classId.get() + ") - " + semester.get() + " " + year.get();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ClassInfo classInfo = (ClassInfo) obj;
        return classId.get().equals(classInfo.classId.get());
    }

    @Override
    public int hashCode() {
        return classId.get().hashCode();
    }
}
