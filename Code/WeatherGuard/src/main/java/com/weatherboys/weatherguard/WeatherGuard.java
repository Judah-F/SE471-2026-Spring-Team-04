package com.weatherboys.weatherguard;

import com.weatherboys.weatherguard.display.Panel_AttendanceData;
import com.weatherboys.weatherguard.display.Panel_AttendanceTable;
import com.weatherboys.weatherguard.display.Panel_WeatherInformation;
import com.weatherboys.weatherguard.display.Panel_WeatherMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class WeatherGuard extends JFrame implements ActionListener {
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;

    private JSplitPane topSplit, botSplit, vertSplit


    public WeatherGuard() {
        setTitle("WeatherGuard");
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(150, 170, 200));
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                // do stuff
            }
        });

        createAndAddPanels();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }

    private void createAndAddPanels() {

        // Use a GridLayout for a 2x2 arrangement
        setLayout(new GridLayout(1, 1));
        setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        // Create displays for all desired information
        Panel_AttendanceTable attendanceTable = new Panel_AttendanceTable();
        Panel_WeatherInformation weatherInformation = new Panel_WeatherInformation();
        Panel_AttendanceData attendanceData = new Panel_AttendanceData();
        Panel_WeatherMap weatherMap = new Panel_WeatherMap();

        topSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, attendanceTable, weatherInformation);
        botSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, attendanceData, weatherMap);
        vertSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topSplit, botSplit); // Intellij complaining for no reason ;-;

        topSplit.setDividerSize(5);
        botSplit.setDividerSize(5);
        vertSplit.setDividerSize(5);

        add(vertSplit);

        topSplit.setDividerLocation((int) (0.75 * WINDOW_WIDTH));
        botSplit.setDividerLocation((int) (0.75 * WINDOW_WIDTH));
        vertSplit.setDividerLocation((int) (0.6 * WINDOW_HEIGHT));

        //Add to frame
    }
}
