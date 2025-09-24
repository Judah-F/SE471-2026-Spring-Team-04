package com.weatherboys.weatherguard.display;

import com.weatherboys.weatherguard.WeatherGuard;

import javax.swing.*;
import java.awt.*;

/**
 * This class is for displaying the current attendance of a class
 * in a table format.
 */
public class Panel_AttendanceTable extends JPanel {

    public Panel_AttendanceTable() {
        setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(new JLabel("Current Session Attendance"));
    }
}
