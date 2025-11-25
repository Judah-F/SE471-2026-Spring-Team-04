package com.weatherboys.ui;

import com.weatherboys.model.ClassInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class TeacherViewController implements Initializable {

    // Selected class data passed from AdminView
    private ClassInfo selectedClass;

    // Buttons
    @FXML
    private Button startSessionButton;
    @FXML
    private Button endSessionButton;
    @FXML
    private Button fiveDayForecastButton2;
    @FXML
    private Button temp2;

    // Student Labels (34 total: student_00 to student_33)
    @FXML
    private Label student_00, student_01, student_02, student_03, student_04, student_05, student_06, student_07,
                  student_08, student_09, student_10, student_11, student_12, student_13, student_14, student_15,
                  student_16, student_17, student_18, student_19, student_20, student_21, student_22, student_23,
                  student_24, student_25, student_26, student_27, student_28, student_29, student_30, student_31,
                  student_32, student_33;

    // Weather Labels
    @FXML
    private Label sunRise2, sunSet2, date2, wind2, humid2, name2, description2, sunriseLabel2, sunsetLabel2;

    // ImageViews
    @FXML
    private ImageView sessionQRCode;
    @FXML
    private ImageView sessionStaticMap;
    @FXML
    private ImageView weatherIcon2;

    // PieChart
    @FXML
    private PieChart sessionPieChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("TeacherViewController initialized");

        // Note: selectedClass will be set via setClassInfo() after initialize()
        // We'll load data in setClassInfo() instead
    }

    /**
     * Sets the class information for this view (called from AdminViewController)
     *
     * @param classInfo The class data to display
     */
    public void setClassInfo(ClassInfo classInfo) {
        this.selectedClass = classInfo;

        // Log the class data
        System.out.println("TeacherView loaded for class: " + classInfo.getClassName());
        System.out.println("  Class ID: " + classInfo.getClassId());
        System.out.println("  Semester: " + classInfo.getFormattedSemester());
        System.out.println("  City: " + classInfo.getCity());
        System.out.println("  Professor: " + classInfo.getProfessorName());

        // TODO: Initialize WeatherService with city
        // TODO: Load students from database for this class
        // TODO: Populate student labels
    }

    @FXML
    public void startSession(ActionEvent event) {
        // TODO: Implement startSession logic
    }

    @FXML
    public void endSession(ActionEvent event) {
        // TODO: Implement endSession logic
    }

    @FXML
    public void switchToFiveDayForecastView(ActionEvent event) {
        // TODO: Implement switchToFiveDayForecastView logic
    }

    @FXML
    public void toggleTemperatureUnit(ActionEvent event) {
        // TODO: Implement toggleTemperatureUnit logic
    }

}
