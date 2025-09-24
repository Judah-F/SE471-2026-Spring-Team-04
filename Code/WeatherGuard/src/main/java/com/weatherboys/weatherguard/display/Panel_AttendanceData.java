package com.weatherboys.weatherguard.display;

import com.weatherboys.weatherguard.WeatherGuard;

import javax.swing.*;
import java.awt.*;

/**
 * This class is for displaying previous attendance data given
 * current weather situation.
 */
public class Panel_AttendanceData extends JPanel {

    public Panel_AttendanceData() {
        setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(new JLabel("Past Session Attendance"));
    }
}
