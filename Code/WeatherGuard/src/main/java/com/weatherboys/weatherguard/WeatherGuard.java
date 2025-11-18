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

    private JSplitPane topSplit, botSplit, vertSplit;


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
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));

        // Create displays for all desired information
        Panel_AttendanceTable attendanceTable = new Panel_AttendanceTable();
        Panel_WeatherInformation weatherInformation = new Panel_WeatherInformation();
        Panel_AttendanceData attendanceData = new Panel_AttendanceData();
        Panel_WeatherMap weatherMap = new Panel_WeatherMap();

        JPanel grid = new JPanel(new GridLayout(2, 2, 6, 6));
        grid.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        grid.add(attendanceTable);
        grid.add(weatherInformation);
        grid.add(attendanceData);
        grid.add(weatherMap);

        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(true);
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT, 6,4));

        JButton newBtn = new JButton("New");
        JButton startSessionBtn = new JButton("Start Session");
        JButton endSessionBtn = new JButton("End Session");
        JButton addStudentBtn = new JButton("Add Student");

        toolbar.add(newBtn);
        toolbar.add(startSessionBtn);
        toolbar.add(endSessionBtn);
        toolbar.add(addStudentBtn);

        //Add to frame
        add(toolbar, BorderLayout.SOUTH);
        add(grid, BorderLayout.CENTER);
    }
}
