package com.weatherboys.weatherguard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WeatherGuard extends JFrame implements ActionListener {
    final int WINDOW_WIDTH = 640;
    final int WINDOW_HEIGHT = 480;

    public WeatherGuard() {
        setTitle("WeatherGuard");
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(0, 136, 170));
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
