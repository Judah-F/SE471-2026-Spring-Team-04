package com.weatherboys.weatherguard.display;

import com.weatherboys.weatherguard.WeatherGuard;

import javax.swing.*;
import java.awt.*;

/**
 * This class is for displaying current weather data and any
 * advisories or warnings.
 */
public class Panel_WeatherInformation extends JPanel {

    public Panel_WeatherInformation() {
        setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(new JLabel("Current Weather"));
    }
}
