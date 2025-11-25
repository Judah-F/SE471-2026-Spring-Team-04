package com.weatherboys.ui;

import com.weatherboys.model.ClassInfo;
import com.weatherboys.weatherguard.Weather.ConfigManager;
import com.weatherboys.weatherguard.Weather.WeatherService;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

public class TeacherViewController implements Initializable {

    // Selected class data passed from AdminView
    private ClassInfo selectedClass;

    // Weather service facade (ONLY import we need for weather!)
    private WeatherService weatherService;

    // Buttons
    @FXML
    private Button startSessionButton;
    @FXML
    private Button endSessionButton;
    @FXML
    private Button fiveDayForecastButton2;
    @FXML
    private Button temp2;

    // Student Labels (34 total: student_00 to student_33)
    @FXML
    private Label student_00, student_01, student_02, student_03, student_04, student_05, student_06, student_07,
                  student_08, student_09, student_10, student_11, student_12, student_13, student_14, student_15,
                  student_16, student_17, student_18, student_19, student_20, student_21, student_22, student_23,
                  student_24, student_25, student_26, student_27, student_28, student_29, student_30, student_31,
                  student_32, student_33;

    // Weather Labels
    @FXML
    private Label sunRise2, sunSet2, date2, wind2, humid2, name2, description2, sunriseLabel2, sunsetLabel2;

    // ImageViews
    @FXML
    private ImageView sessionQRCode;
    @FXML
    private ImageView sessionStaticMap;
    @FXML
    private ImageView weatherIcon2;

    // PieChart
    @FXML
    private PieChart sessionPieChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Note: selectedClass will be set via setClassInfo() after initialize()
        // We'll load data in setClassInfo() instead
    }

    /**
     * Sets the class information for this view (called from AdminViewController)
     *
     * @param classInfo The class data to display
     */
    public void setClassInfo(ClassInfo classInfo) {
        this.selectedClass = classInfo;

        // Initialize WeatherService facade with city from class
        initializeWeather();

        // TODO: Load students from database for this class
        // TODO: Populate student labels
    }

    /**
     * Initializes WeatherService and loads weather data
     * Uses Facade pattern - only interacts with WeatherService, not individual weather classes
     */
    private void initializeWeather() {
        try {
            // Get API key from config
            Properties config = ConfigManager.loadConfig();
            String apiKey = config.getProperty("apiKey");

            // Create WeatherService facade with city from selected class
            weatherService = new WeatherService(apiKey, selectedClass.getCity());

            // Get all weather data through the facade
            Map<String, Object> weatherData = weatherService.getAllWeatherInfo();

            // Display weather information
            displayWeather(weatherData);

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Weather Error",
                "Failed to load weather data: " + e.getMessage());
        }
    }

    /**
     * Displays weather information in the UI
     * @param weatherData Map containing weather, forecast, and map from WeatherService
     */
    private void displayWeather(Map<String, Object> weatherData) {
        // Get individual components from facade response
        var weather = weatherData.get("weather");
        var forecast = weatherData.get("forecast");
        var map = weatherData.get("map");

        // Use reflection to safely extract weather data
        try {
            if (weather != null) {
                // Display current weather
                name2.setText(weather.getClass().getMethod("getName").invoke(weather).toString());
                description2.setText(weather.getClass().getMethod("getDescription").invoke(weather).toString());

                Object temp = weather.getClass().getMethod("getCurrentTemp").invoke(weather);
                // Temperature display will be handled by temp2 button

                Object humidity = weather.getClass().getMethod("getHumidity").invoke(weather);
                humid2.setText(humidity + "%");

                Object windSpeed = weather.getClass().getMethod("getWind").invoke(weather);
                wind2.setText(windSpeed + " mph");

                Object sunrise = weather.getClass().getMethod("getSunRise").invoke(weather);
                String sunriseTime = (String) weather.getClass().getMethod("convertSunRiseSunSet", long.class).invoke(weather, (Long) sunrise);
                sunRise2.setText(sunriseTime);

                Object sunset = weather.getClass().getMethod("getSunSet").invoke(weather);
                String sunsetTime = (String) weather.getClass().getMethod("convertSunRiseSunSet", long.class).invoke(weather, (Long) sunset);
                sunSet2.setText(sunsetTime);

                String dateStr = (String) weather.getClass().getMethod("getDate").invoke(weather);
                date2.setText(dateStr);

                // Load weather icon
                String iconCode = (String) weather.getClass().getMethod("getIcon").invoke(weather);
                loadWeatherIcon(iconCode);
            }

            if (map != null) {
                // Display weather map
                var mapImage = map.getClass().getMethod("getMapImage").invoke(map);
                if (mapImage != null) {
                    Image fxImage = SwingFXUtils.toFXImage((java.awt.image.BufferedImage) mapImage, null);
                    sessionStaticMap.setImage(fxImage);
                }
            }

        } catch (Exception e) {
            // e.printStackTrace();
            showAlert(Alert.AlertType.WARNING, "Display Error",
                "Weather data loaded but some information could not be displayed");
        }
    }

    /**
     * Loads weather icon from OpenWeatherMap
     */
    private void loadWeatherIcon(String iconCode) {
        String iconUrl = "http://openweathermap.org/img/wn/" + iconCode + "@2x.png";
        Image icon = new Image(iconUrl);
        weatherIcon2.setImage(icon);
    }

    @FXML
    public void startSession(ActionEvent event) {
        // TODO: Implement startSession logic
    }

    @FXML
    public void endSession(ActionEvent event) {
        // TODO: Implement endSession logic
    }

    @FXML
    public void switchToFiveDayForecastView(ActionEvent event) {
        try {
            // Pass forecast data to FiveDayForecastController using Facade pattern
            if (weatherService != null) {
                var forecast = weatherService.getFiveDayForecast();
                var weather = weatherService.getCurrentWeatherData();

                // Extract location info using reflection (to maintain facade pattern)
                String cityName = (String) weather.getClass().getMethod("getName").invoke(weather);
                String country = (String) weather.getClass().getMethod("getCountry").invoke(weather);

                FiveDayForecastController.setCurrentForecast(forecast, cityName, country);
            }

            // Load FiveDayForecastView
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/FiveDayForecastView.fxml"));
            Stage stage = (Stage) fiveDayForecastButton2.getScene().getWindow();
            Scene scene = new Scene(root, 900, 600);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            // e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                "Failed to load 5-Day Forecast view: " + e.getMessage());
        }
    }

    @FXML
    public void toggleTemperatureUnit(ActionEvent event) {
        // TODO: Implement toggleTemperatureUnit logic (Fahrenheit/Celsius toggle)
    }

    /**
     * Helper method to show alerts
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
