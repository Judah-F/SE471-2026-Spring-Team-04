package com.weatherboys.weatherguard.Weather;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

public class Weather {

    private static final Logger logger = Logger.getLogger(Weather.class.getName());
    private int currentTemp, maxTemp, minTemp, humidity, visibility, timezone;
    private long sunrise, sunset, time;
    private double lon, lat, wind;
    private String description, country, name, icon;
    private static final Properties properties = new Properties();

    // Default constructor for creating an empty Weather object
    public Weather() {
    }

    // Parameterized constructor for creating a Weather object with specific data
    public Weather(int currentTemp, int maxTemp, int minTemp, int humidity, int visibility, int timezone, long sunrise, long sunset, long time, double lon, double lat, double wind, String description, String country, String name, String icon) {
        if (currentTemp < -100 || currentTemp > 150) {
            throw new IllegalArgumentException("Current temperature out of range: " + currentTemp);
        }
        if (maxTemp < -100 || maxTemp > 150) {
            throw new IllegalArgumentException("Max temperature out of range: " + maxTemp);
        }
        if (minTemp < -100 || minTemp > 150) {
            throw new IllegalArgumentException("Min temperature out of range: " + minTemp);
        }
        if (humidity < 0 || humidity > 100) {
            throw new IllegalArgumentException("Humidity out of range: " + humidity);
        }
        if (visibility < 0) {
            throw new IllegalArgumentException("Visibility must be non-negative: " + visibility);
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description must not be null or empty");
        }
        if (country == null || country.isEmpty()) {
            throw new IllegalArgumentException("Country must not be null or empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name must not be null or empty");
        }
        if (icon == null || icon.isEmpty()) {
            throw new IllegalArgumentException("Icon must not be null or empty");
        }

        this.currentTemp = currentTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.humidity = humidity;
        this.visibility = visibility;
        this.timezone = timezone;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.time = time;
        this.lon = lon;
        this.lat = lat;
        this.wind = wind;
        this.description = description;
        this.country = country;
        this.name = name;
        this.icon = icon;
    }

    // Create a Weather object for a given city
    public static Weather fetchWeatherForCity(String city) {
        String apiKey = null;
        try {
            Properties config = ConfigManager.loadConfig();
            apiKey = config.getProperty("apiKey");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load API key from config", e);
        }

        if (apiKey == null || apiKey.trim().isEmpty()) {
            logger.log(Level.SEVERE, "API key is missing or empty in config");
            return new Weather(); // Return an empty Weather object
        }

        String requestURL = "http://api.openweathermap.org/data/2.5/weather?q=" + city.replace(" ", "+") + "&APPID=" + apiKey;
        HttpResponse<String> response = invokeGET(requestURL);

        if (response == null || response.body() == null || response.statusCode() != 200) {
            return new Weather(); // Return an empty Weather object
        }

        JSONObject obj = new JSONObject(response.body());
        return parseWeatherData(obj);
    }

    // Parse weather data from JSON response and return a Weather object
    private static Weather parseWeatherData(JSONObject obj) {
        JSONArray weatherArray = obj.getJSONArray("weather");
        JSONObject mainObj = obj.getJSONObject("main");
        JSONObject sysObj = obj.getJSONObject("sys");
        JSONObject coordObj = obj.getJSONObject("coord");
        JSONObject windObj = obj.getJSONObject("wind");
        JSONObject weatherDesc = weatherArray.getJSONObject(0);

        String icon = weatherDesc.getString("icon");
        String name = obj.getString("name");
        double wind = windObj.getDouble("speed");
        String description = weatherDesc.getString("description");
        int currentTemp = kelvinToFahrenheit(mainObj.getDouble("temp"));
        int maxTemp = kelvinToFahrenheit(mainObj.getDouble("temp_max"));
        int minTemp = kelvinToFahrenheit(mainObj.getDouble("temp_min"));
        int humidity = mainObj.getInt("humidity");
        int visibility = (int) (obj.getDouble("visibility") / 1000);
        long sunrise = sysObj.getLong("sunrise");
        long sunset = sysObj.getLong("sunset");
        String country = sysObj.getString("country");
        int timezone = obj.getInt("timezone");
        double lon = coordObj.getDouble("lon");
        double lat = coordObj.getDouble("lat");
        long time = obj.getLong("dt");

        return new Weather(currentTemp, maxTemp, minTemp, humidity, visibility, timezone, sunrise, sunset, time, lon, lat, wind, description, country, name, icon);
    }

    // Method to check if the Weather object is empty
    public boolean isEmpty() {
        return name == null || name.isEmpty();
    }

    // Convert temperature from Kelvin to Fahrenheit
    private static int kelvinToFahrenheit(double kelvin) {
        return (int) Math.round((kelvin - 273.15) * 1.8 + 32);
    }

    // Convert sunrise and sunset times to readable format
    public String convertSunRiseSunSet(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm aa");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        time = time * 1000 + (getTimezone() * 1000L);
        Date date = new Date(time);
        return sdf.format(date);
    }

    // Set date and time to readable format
    public String getDate() {
        SimpleDateFormat dateObj = new SimpleDateFormat("MMMM dd,\nhh:mm aa");
        dateObj.setTimeZone(TimeZone.getTimeZone("UTC"));
        long times = getTime() * 1000 + (getTimezone() * 1000L);
        Date tempDate = new Date(times);
        return dateObj.format(tempDate);
    }

    // Getters for weather data fields
    public String getIcon() { return icon; }
    public int getCurrentTemp() { return currentTemp; }
    public int getMaxTemp() { return maxTemp; }
    public int getMinTemp() { return minTemp; }
    public String getDescription() { return description; }
    public String getName() { return name; }
    public int getHumidity() { return humidity; }
    public int getVisibility() { return visibility; }
    public long getSunRise() { return sunrise; }
    public long getSunSet() { return sunset; }
    public int getTimezone() { return timezone; }
    public double getLon() { return lon; }
    public double getLat() { return lat; }
    public double getWind() { return wind; }
    public long getTime() { return time; }
    public String getCountry() { return country; }

    // Setters for weather data fields
    public void setTime(long time) { this.time = time; }
    public void setTimezone(int timezone) { this.timezone = timezone; }

    @Override
    public String toString() {
        return "City: " + name + "\n" +
               "Current Temp: " + currentTemp + "\n" +
               "Description: " + description + "\n" +
               "Humidity: " + humidity + "\n" +
               "Wind: " + wind + "\n";
    }

    // Invoke a GET request to the given URL
    static HttpResponse<String> invokeGET(String requestURL) {
        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(requestURL)).GET().build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            logger.log(Level.SEVERE, "ERROR:Weather:HttpResponse", e);
        }
        return response;
    }

    // Getter for properties for testing purposes
    static Properties getProperties() {
        return properties;
    }
}
