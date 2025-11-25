package com.weatherboys.weatherguard.Weather;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * WeatherService - Facade Pattern Implementation
 *
 * This class provides a simplified interface to the Weather subsystem.
 * Instead of controllers dealing with Weather, Forecast, and StaticMap separately,
 * they can use this single WeatherService facade to access all weather-related functionality.
 *
 * Benefits:
 * - Single entry point for all weather operations
 * - Hides the complexity of coordinating multiple subsystem classes
 * - Makes controller code cleaner and easier to maintain
 */
public class WeatherService {

    private static final Logger logger = Logger.getLogger(WeatherService.class.getName());

    // Private references to subsystem components
    private Weather weather;
    private Forecast forecast;
    private StaticMap staticMap;

    // Configuration
    private String apiKey;
    private String city;
    private boolean useFahrenheit;

    /**
     * Constructor for WeatherService
     *
     * @param apiKey The OpenWeatherMap API key
     * @param city The city name to fetch weather data for
     */
    public WeatherService(String apiKey, String city) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("API key cannot be null or empty");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }

        this.apiKey = apiKey;
        this.city = city;
        this.useFahrenheit = true; // Default to Fahrenheit

        // Initialize all weather data on construction
        refreshAllData();
    }

    /**
     * Get current weather data for the city
     *
     * @return Weather object containing current weather information
     */
    public Weather getCurrentWeatherData() {
        if (weather == null || weather.isEmpty()) {
            logger.log(Level.WARNING, "Weather data is empty, attempting to refresh");
            weather = Weather.fetchWeatherForCity(city);
        }
        return weather;
    }

    /**
     * Get 5-day weather forecast for the city
     *
     * @return Forecast object containing 5-day forecast data
     */
    public Forecast getFiveDayForecast() {
        if (forecast == null || forecast.isEmpty()) {
            logger.log(Level.WARNING, "Forecast data is empty, attempting to refresh");
            // Forecast needs lat/lon from current weather
            if (weather == null || weather.isEmpty()) {
                weather = Weather.fetchWeatherForCity(city);
            }
            if (weather != null && !weather.isEmpty()) {
                forecast = Forecast.fetchForecastForLocation(weather.getLat(), weather.getLon());
            }
        }
        return forecast;
    }

    /**
     * Get weather map for the city location
     *
     * @param layer The weather layer to display (e.g., "temp_new", "precipitation_new", "clouds_new")
     * @return StaticMap object containing the weather map image
     */
    public StaticMap getWeatherMap(String layer) {
        if (staticMap == null || staticMap.isEmpty()) {
            logger.log(Level.WARNING, "Static map is empty, attempting to refresh");
            // StaticMap needs lat/lon from current weather
            if (weather == null || weather.isEmpty()) {
                weather = Weather.fetchWeatherForCity(city);
            }
            if (weather != null && !weather.isEmpty()) {
                staticMap = StaticMap.fetchMapForLocation(weather.getLat(), weather.getLon());
            }
        }
        return staticMap;
    }

    /**
     * Get all weather information in one call
     * This is the main facade method that simplifies access to all weather data
     *
     * @return Map containing "weather", "forecast", and "map" keys with their respective objects
     */
    public Map<String, Object> getAllWeatherInfo() {
        Map<String, Object> allData = new HashMap<>();

        // Get all data through the facade methods
        Weather w = getCurrentWeatherData();
        Forecast f = getFiveDayForecast();
        StaticMap m = getWeatherMap("clouds_new");

        allData.put("weather", w);
        allData.put("forecast", f);
        allData.put("map", m);

        // logger.log(Level.INFO, "Retrieved all weather info for city: " + city);

        return allData;
    }

    /**
     * Refresh all weather data by fetching fresh data from the API
     * Useful when you want to update the weather information
     */
    public void refreshAllData() {
        // logger.log(Level.INFO, "Refreshing all weather data for city: " + city);

        // Fetch fresh weather data
        weather = Weather.fetchWeatherForCity(city);

        // Only fetch forecast and map if we have valid weather data with coordinates
        if (weather != null && !weather.isEmpty()) {
            forecast = Forecast.fetchForecastForLocation(weather.getLat(), weather.getLon());
            staticMap = StaticMap.fetchMapForLocation(weather.getLat(), weather.getLon());
            // logger.log(Level.INFO, "Successfully refreshed all weather data");
        } else {
            logger.log(Level.SEVERE, "Failed to refresh weather data - Weather object is empty");
        }
    }

    /**
     * Update the city and refresh all data
     *
     * @param newCity The new city name
     */
    public void setCity(String newCity) {
        if (newCity == null || newCity.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }
        this.city = newCity;
        refreshAllData();
    }

    // Getters
    public String getCity() {
        return city;
    }

    public String getApiKey() {
        return apiKey;
    }

    public boolean isUsingFahrenheit() {
        return useFahrenheit;
    }

    /**
     * Set temperature unit preference
     *
     * @param useFahrenheit true for Fahrenheit, false for Celsius
     */
    public void setTemperatureUnit(boolean useFahrenheit) {
        this.useFahrenheit = useFahrenheit;
    }

    /**
     * Get formatted temperature string based on current unit preference
     *
     * @param tempFahrenheit Temperature in Fahrenheit
     * @param tempCelsius Temperature in Celsius
     * @return Formatted temperature string with unit symbol
     */
    public String getFormattedTemperature(int tempFahrenheit, int tempCelsius) {
        if (useFahrenheit) {
            return tempFahrenheit + "°F";
        } else {
            return tempCelsius + "°C";
        }
    }

    /**
     * Check if the service has valid data
     *
     * @return true if all subsystem components have valid data
     */
    public boolean hasValidData() {
        return weather != null && !weather.isEmpty() &&
               forecast != null && !forecast.isEmpty() &&
               staticMap != null && !staticMap.isEmpty();
    }
}
