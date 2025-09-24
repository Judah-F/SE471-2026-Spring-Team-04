package com.weatherboys.weatherguard.display;

import com.weatherboys.weatherguard.WeatherGuard;

import javax.swing.*;
import java.awt.*;

/**
 * This class is for displaying the current weather map
 */
public class Panel_WeatherMap extends JPanel {

    public Panel_WeatherMap() {
        setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(new JLabel("Map"));
    }
}
