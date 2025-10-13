package com.weatherboys;

import com.weatherboys.weatherguard.Weather.StaticMap;
import com.weatherboys.weatherguard.Weather.Forecast;
import com.weatherboys.weatherguard.Weather.Weather;
import com.weatherboys.weatherguard.WeatherGuard;

import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        // Test Weather obj
        Weather weather = Weather.fetchWeatherForCity("Tokyo");
        System.out.println(weather);

        // Test Forecast obj
        Forecast forecast = Forecast.fetchForecastForLocation(weather.getLat(), weather.getLon());
        System.out.println(forecast);

        // Test StaticMap
        testWeatherMap();

        SwingUtilities.invokeLater(WeatherGuard::new);
    }

    private static void testWeatherMap() {
        System.out.println("Testing StaticMap...");

        Weather w = Weather.fetchWeatherForCity("Esbjerg");
        System.out.println("City: " + w.getName());
        System.out.println("Lat: " + w.getLat() + ", Lon: " + w.getLon());

        StaticMap map = StaticMap.fetchMapForLocation(w.getLat(), w.getLon());

        if (!map.isEmpty()) {
            System.out.println("Image size: " + map.getMapImage().getWidth() + "x" + map.getMapImage().getHeight());
            // Show it in a test window
            JFrame testFrame = new JFrame("Map Test");
            testFrame.add(new JLabel(new ImageIcon(map.getMapImage())));
            testFrame.pack();
            testFrame.setVisible(true);
        } else {
            System.out.println("Failed to fetch map");
        }
    }
}