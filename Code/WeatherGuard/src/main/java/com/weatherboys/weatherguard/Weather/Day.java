package com.weatherboys.weatherguard.Weather;

public class Day {
    private long timestamp;
    private double highTemp;
    private double lowTemp;
    private String description;
    private String icon;

    public Day() {
    }

    public Day(long timestamp, double highTemp, double lowTemp, String description, String icon) {
        this.timestamp = timestamp;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
        this.description = description;
        this.icon = icon;
    }

    // Getters and Setters
    public long getTimestamp() { return timestamp; }
    public double getHighTemp() { return highTemp; }
    public double getLowTemp() { return lowTemp; }
    public String getDescription() { return description; }
    public String getIcon() { return icon; }

    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public void setHighTemp(double highTemp) { this.highTemp = highTemp; }
    public void setLowTemp(double lowTemp) { this.lowTemp = lowTemp; }
    public void setDescription(String description) { this.description = description; }
    public void setIcon(String icon) { this.icon = icon; }

    // Method to check if the day is empty
    public boolean isEmpty() {
        return timestamp == 0 && highTemp == 0 && lowTemp == 0 
                && (description == null || description.isEmpty()) 
                && (icon == null || icon.isEmpty());
    }

    // Override toString method for better logging
    @Override
    public String toString() {
        return "Day{" +
                "timestamp=" + timestamp +
                ", highTemp=" + highTemp +
                ", lowTemp=" + lowTemp +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
