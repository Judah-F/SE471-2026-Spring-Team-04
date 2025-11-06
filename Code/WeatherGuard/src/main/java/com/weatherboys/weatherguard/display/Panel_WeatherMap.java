package com.weatherboys.weatherguard.display;

import com.weatherboys.weatherguard.Weather.StaticMap;
import com.weatherboys.weatherguard.Weather.Weather;
import com.weatherboys.weatherguard.WeatherGuard;

import javax.swing.*;
import java.awt.*;

/**
 * This class is for displaying the current weather map
 */
public class Panel_WeatherMap extends JPanel {

    public Panel_WeatherMap() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.LIGHT_GRAY);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        //TODO: Change this to be dynamically changed
        Weather w = Weather.fetchWeatherForCity("Esbjerg");
        System.out.println("City: " + w.getName());
        System.out.println("Lat: " + w.getLat() + ", Lon: " + w.getLon());

        add(new JLabel("Map", SwingConstants.CENTER));
        StaticMap map = StaticMap.fetchMapForLocation(w.getLat(), w.getLon());

        if (!map.isEmpty()) {
            add(new JLabel(new ImageIcon(map.getMapImage()), SwingConstants.CENTER));
        } else {
            add(new JLabel("Failed to fetch map", SwingConstants.CENTER));
        }
    }
}
