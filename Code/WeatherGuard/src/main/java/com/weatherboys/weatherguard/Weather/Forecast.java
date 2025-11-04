package com.weatherboys.weatherguard.Weather;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

public class Forecast {
    private static final Logger logger = Logger.getLogger(Forecast.class.getName());
    private double lat, lon; // Latitude and longitude to generate URL request
    private int timeZone; // Timezone of the forecast location
    private final List<Day> days; // List to store forecast data for multiple days
    
    // Default constructor initializes an empty list of days
    public Forecast() {
        days = new ArrayList<>();
    }

    // Parameterized constructor initializes all fields
    public Forecast(double lat, double lon, int timeZone, List<Day> days) {
        if (days == null) {
            throw new IllegalArgumentException("ERROR:Forecast:Days list must not be null");
        }
        this.lat = lat;
        this.lon = lon;
        this.timeZone = timeZone;
        this.days = days;
    }

    // Static factory method to fetch forecast for a specific location
    public static Forecast fetchForecastForLocation(double lat, double lon) {
        // Validate latitude and longitude values
        if (lat < -90 || lat > 90 || lon < -180 || lon > 180) {
            logger.log(Level.SEVERE, "Invalid latitude or longitude values.");
            return new Forecast(); // Return an empty Forecast object
        }

        String apiKey = null;
        try {
            Properties config = ConfigManager.loadConfig();
            apiKey = config.getProperty("apiKey");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load API key from config", e);
        }

        if (apiKey == null || apiKey.trim().isEmpty()) {
            logger.log(Level.SEVERE, "API key is missing or empty in config");
            return new Forecast(); // Return an empty Forecast object
        }

        String requestURL = String.format("http://api.openweathermap.org/data/2.5/forecast?lat=%f&lon=%f&appid=%s", lat, lon, apiKey);
        HttpResponse<String> response = invokeGET(requestURL); // Fetch data from API

        // Check if the response is valid
        if (response == null || response.body() == null || response.statusCode() != 200) {
            logger.log(Level.SEVERE, "Failed to fetch forecast data. Response code: " + (response != null ? response.statusCode() : "null"));
            return new Forecast(); // Return an empty Forecast object
        }

        // Parse the JSON response and create a Forecast object
        JSONObject obj = new JSONObject(response.body());
        return parseForecastData(obj, lat, lon);
    }

    // Parse JSON data to create a Forecast object
    private static Forecast parseForecastData(JSONObject obj, double lat, double lon) {
        JSONArray weatherArray = obj.getJSONArray("list"); // Get the list of weather data entries
        List<Day> days = new ArrayList<>();
        int timeZone = obj.getJSONObject("city").getInt("timezone"); // Get the timezone from the response

        // Process each day in the weather array
        for (int i = 0; i < weatherArray.length(); i += 8) { // Every 8 entries represent a new day (3-hour intervals, 24 hours/day)
            double highTemp = Double.MIN_VALUE;
            double lowTemp = Double.MAX_VALUE;
            long timestamp = 0;
            String description = "";
            String icon = "";

            // Process each 3-hour entry within the day
            for (int j = i; j < i + 8 && j < weatherArray.length(); j++) {
                JSONObject dayForecast = weatherArray.getJSONObject(j);
                JSONObject weatherObject = dayForecast.getJSONArray("weather").getJSONObject(0);
                description = weatherObject.getString("main");
                icon = weatherObject.getString("icon");

                JSONObject temperatureObject = dayForecast.getJSONObject("main");
                double high = kelvinToFahrenheit(temperatureObject.getDouble("temp_max"));
                double low = kelvinToFahrenheit(temperatureObject.getDouble("temp_min"));

                // Update high and low temperatures for the day
                if (high > highTemp) {
                    highTemp = high;
                }
                if (low < lowTemp) {
                    lowTemp = low;
                }
                timestamp = dayForecast.getLong("dt");
            }

            // Add the day's forecast to the list
            days.add(new Day(timestamp, highTemp, lowTemp, description, icon));
        }

        // Return the populated Forecast object
        return new Forecast(lat, lon, timeZone, days);
    }

    public boolean isEmpty() {
        return days.isEmpty() && lat == 0 && lon == 0;
    }

    // Convert Unix time to a formatted date string
    public String convertTime(long time, int timeZone) {
        time = time * 1000;
        time = time + timeZone * 1000L;
        Date tempDate = new Date(time);
        SimpleDateFormat dateObj = new SimpleDateFormat("MMM, dd");
        dateObj.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateObj.format(tempDate);
    }

    // Convert temperature from Kelvin to Fahrenheit
    private static double kelvinToFahrenheit(double kelvin) {
        kelvin = kelvin - 273.15;
        kelvin = kelvin * 1.8;
        kelvin = kelvin + 32;
        return kelvin;
    }

    // Helper method to invoke a GET request
    public static HttpResponse<String> invokeGET(String requestURL) {
        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(requestURL)).GET().build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            logger.log(Level.SEVERE, "ERROR:Forecast:HttpResponse", e);
        }
        return response;
    }

    // Getters and Setters
    public double getLat() { return lat; }
    public void setLat(double lat) { this.lat = lat; }

    public double getLon() { return lon; }
    public void setLon(double lon) { this.lon = lon; }

    public int getTimeZone() { return timeZone; }
    public void setTimeZone(int timeZone) { this.timeZone = timeZone; }

    public List<Day> getDays() { return days; }

    @Override
    public String toString() {
        String result = "Forecast for location (" + lat + ", " + lon + "):\n";
        result += "Timezone: " + timeZone + "\n";
        result += "Number of days: " + days.size() + "\n\n";

        for (int i = 0; i < days.size(); i++) {
            Day day = days.get(i);
            result += "Day " + (i + 1) + ":\n";
            result += "  Date: " + convertTime(day.getTimestamp(), timeZone) + "\n";
            result += "  High: " + String.format("%.2f", day.getHighTemp()) + " F\n";
            result += "  Low: " + String.format("%.2f", day.getLowTemp()) + " F\n";
            result += "  Description: " + day.getDescription() + "\n";
            result += "  Icon: " + day.getIcon() + "\n\n";
        }

        return result;
    }
}
