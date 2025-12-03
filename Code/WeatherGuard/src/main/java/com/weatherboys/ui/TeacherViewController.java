package com.weatherboys.ui;

import com.weatherboys.model.ClassInfo;
import com.weatherboys.model.Student;
import com.weatherboys.weatherguard.DatabaseManager;
import com.weatherboys.weatherguard.QRCodeGenerator;
import com.weatherboys.weatherguard.Weather.ConfigManager;
import com.weatherboys.weatherguard.Weather.WeatherService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.bson.Document;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TeacherViewController implements Initializable {

    // Selected class data passed from AdminView
    private ClassInfo selectedClass;

    // Weather service facade
    private WeatherService weatherService;

    // Store current temperatures (both F and C from Weather object)
    private int currentTempFahrenheit;
    private int currentTempCelsius;

    // Database manager for student data
    private DatabaseManager dbManager;

    // Student tracking
    private List<Student> classStudents;
    private Map<String, Label> studentLabelMap; // Maps studentId to label
    private Map<String, String> studentStatusMap; // Maps studentId to status: "gray", "red", "green"
    private boolean sessionActive = false;

    // Session tracking
    private String currentSessionId;
    private Timeline attendancePollingTimer;

    // Buttons
    @FXML
    private Button startSessionButton;
    @FXML
    private Button endSessionButton;
    @FXML
    private Button fiveDayForecastButton2;
    @FXML
    private Button temp2;
    @FXML
    private Button adminViewButton;

    // Student Labels (34 total: student_00 to student_33)
    @FXML
    private Label student_00, student_01, student_02, student_03, student_04, student_05, student_06, student_07,
                  student_08, student_09, student_10, student_11, student_12, student_13, student_14, student_15,
                  student_16, student_17, student_18, student_19, student_20, student_21, student_22, student_23,
                  student_24, student_25, student_26, student_27, student_28, student_29, student_30, student_31,
                  student_32, student_33;

    // Class Info Labels
    @FXML
    private Label classNameLabel, classIDLabel, professorLabel;

    // Weather Labels
    @FXML
    private Label sunRise2, sunSet2, date2, wind2, humid2, name2, description2;

    // ImageViews
    @FXML
    private ImageView sessionQRCode;
    @FXML
    private ImageView sessionStaticMap;
    @FXML
    private ImageView weatherIcon2;
    @FXML
    private ImageView sunRiseImage;
    @FXML
    private ImageView sunSetImage;
    @FXML
    private ImageView humidityImage;
    @FXML
    private ImageView windImage;

    // PieChart
    @FXML
    private PieChart sessionPieChart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize database manager
        dbManager = DatabaseManager.getInstance();

        // Initialize student tracking collections
        classStudents = new ArrayList<>();
        studentLabelMap = new HashMap<>();
        studentStatusMap = new HashMap<>();

        // Create array of all 34 student labels for easy iteration
        Label[] allLabels = {
            student_00, student_01, student_02, student_03, student_04, student_05, student_06, student_07,
            student_08, student_09, student_10, student_11, student_12, student_13, student_14, student_15,
            student_16, student_17, student_18, student_19, student_20, student_21, student_22, student_23,
            student_24, student_25, student_26, student_27, student_28, student_29, student_30, student_31,
            student_32, student_33
        };

        // Initialize all labels: gray background and hidden by default
        for (Label label : allLabels) {
            label.setStyle("-fx-background-color: #808080;"); // Gray
            label.setVisible(false);
            label.setText("");
        }

        // Initialize button visibility - only show Start Session button initially
        startSessionButton.setVisible(true);
        startSessionButton.setDisable(false);
        endSessionButton.setVisible(false);
        endSessionButton.setDisable(true);

        // Initialize display visibility: hide QR code, show pie chart initially
        if (sessionQRCode != null) {
            sessionQRCode.setVisible(false);
        }
        if (sessionPieChart != null) {
            sessionPieChart.setVisible(true);
            sessionPieChart.setTitle("Previous Session");
        }

        // Load sunrise and sunset images
        loadSunriseAndSunsetImages();

        // Note: selectedClass will be set via setClassInfo() after initialize()
        // We will load students and previous session data in setClassInfo()
    }

    /**
     * Sets the class information for this view (called from AdminViewController)
     *
     * @param classInfo The class data to display
     */
    public void setClassInfo(ClassInfo classInfo) {
        this.selectedClass = classInfo;

        // Update class info labels
        updateClassInfoLabels();

        // Initialize WeatherService facade with city from class
        initializeWeather();

        // Load students from database for this class
        loadStudents();

        // Load previous session data for pie chart
        loadPreviousSessionData();
    }

    /**
     * Sets the class information and temperature unit preference
     * (called when returning from FiveDayForecastView)
     *
     * @param classInfo The class data to display
     * @param useFahrenheit Whether to display temperature in Fahrenheit
     */
    public void setClassInfo(ClassInfo classInfo, boolean useFahrenheit) {
        this.selectedClass = classInfo;

        // Update class info labels
        updateClassInfoLabels();

        // Initialize WeatherService facade with city from class
        initializeWeather();

        // Set temperature unit preference in the facade
        if (weatherService != null) {
            weatherService.setTemperatureUnit(useFahrenheit);
        }

        // Update temperature display with the correct unit
        updateTemperatureDisplay();

        // Load students from database for this class
        loadStudents();

        // Load previous session data for pie chart
        loadPreviousSessionData();
    }

    /**
     * Updates the class info labels with data from selectedClass
     */
    private void updateClassInfoLabels() {
        if (selectedClass == null) {
            return;
        }

        if (classNameLabel != null) {
            classNameLabel.setText(selectedClass.getClassName());
        }
        if (classIDLabel != null) {
            classIDLabel.setText(selectedClass.getClassId());
        }
        if (professorLabel != null) {
            professorLabel.setText(selectedClass.getProfessorName());
        }
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
     * Updates the temperature display based on current unit preference in WeatherService
     */
    private void updateTemperatureDisplay() {
        if (weatherService != null && temp2 != null) {
            String formattedTemp = weatherService.getFormattedTemperature(currentTempFahrenheit, currentTempCelsius);
            temp2.setText(formattedTemp);
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
                // Display current weather with city and country
                String cityName = weather.getClass().getMethod("getName").invoke(weather).toString();
                String country = weather.getClass().getMethod("getCountry").invoke(weather).toString();
                name2.setText(cityName + ", " + country);
                description2.setText(weather.getClass().getMethod("getDescription").invoke(weather).toString());

                // Store both F and C temperatures from Weather object
                currentTempFahrenheit = (Integer) weather.getClass().getMethod("getCurrentTemp").invoke(weather);
                currentTempCelsius = (Integer) weather.getClass().getMethod("getCurrentTempC").invoke(weather);

                // Display temperature using WeatherService facade
                updateTemperatureDisplay();

                Object humidity = weather.getClass().getMethod("getHumidity").invoke(weather);
                humid2.setText(humidity + "% Humidity");

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

    /**
     * Loads weather icon images from resources
     */
    private void loadSunriseAndSunsetImages() {
        try {
            // Load sunrise image
            if (sunRiseImage != null) {
                Image sunriseImg = new Image(getClass().getResourceAsStream("/png/sunrise-48.png"));
                sunRiseImage.setImage(sunriseImg);
            }

            // Load sunset image
            if (sunSetImage != null) {
                Image sunsetImg = new Image(getClass().getResourceAsStream("/png/sunset-48.png"));
                sunSetImage.setImage(sunsetImg);
            }

            // Load humidity image
            if (humidityImage != null) {
                Image humidityImg = new Image(getClass().getResourceAsStream("/png/humidity-100.png"));
                humidityImage.setImage(humidityImg);
            }

            // Load wind image
            if (windImage != null) {
                Image windImg = new Image(getClass().getResourceAsStream("/png/wind-96.png"));
                windImage.setImage(windImg);
            }
        } catch (Exception e) {
            // If images fail to load, just continue without them
            System.err.println("Could not load weather images: " + e.getMessage());
        }
    }

    /**
     * Loads students from the database and displays them alphabetically in labels
     */
    private void loadStudents() {
        if (selectedClass == null) {
            return;
        }

        // Clear existing data
        classStudents.clear();
        studentLabelMap.clear();
        studentStatusMap.clear();

        // Fetch students from database
        List<Document> studentDocs = dbManager.getStudentsByClass(selectedClass.getClassId());

        // Convert to Student objects and sort alphabetically by name
        classStudents = studentDocs.stream()
            .map(doc -> new Student(
                doc.getString("studentId"),
                doc.getString("studentName"),
                doc.getString("classId")
            ))
            .sorted(Comparator.comparing(Student::getStudentName))
            .collect(Collectors.toList());

        // Create array of all 34 student labels
        Label[] allLabels = {
            student_00, student_01, student_02, student_03, student_04, student_05, student_06, student_07,
            student_08, student_09, student_10, student_11, student_12, student_13, student_14, student_15,
            student_16, student_17, student_18, student_19, student_20, student_21, student_22, student_23,
            student_24, student_25, student_26, student_27, student_28, student_29, student_30, student_31,
            student_32, student_33
        };

        // Assign students to labels (max 34 students)
        for (int i = 0; i < Math.min(classStudents.size(), 34); i++) {
            Student student = classStudents.get(i);
            Label label = allLabels[i];

            // Set label text to show student name and ID
            label.setText(student.getStudentName() + ", " + student.getStudentId());

            // Make label visible
            label.setVisible(true);

            // Keep gray background (default state before session starts)
            label.setStyle("-fx-background-color: #808080;"); // Gray

            // Map student ID to label for later status updates
            studentLabelMap.put(student.getStudentId(), label);

            // Initialize status as gray
            studentStatusMap.put(student.getStudentId(), "gray");
        }
    }

    /**
     * Loads the most recent session data for this class and displays in pie chart
     */
    private void loadPreviousSessionData() {
        if (selectedClass == null) {
            return;
        }

        try {
            // Get all attendance records for this class
            List<Document> allAttendance = dbManager.getAttendanceByClass(selectedClass.getClassId());

            if (allAttendance.isEmpty()) {
                // No previous sessions - show empty pie chart
                updatePieChart(0, 0, "No Previous Session");
                return;
            }

            // Find the most recent session by grouping attendance by sessionId
            Map<String, List<Document>> sessionGroups = new HashMap<>();
            for (Document record : allAttendance) {
                String sessionId = record.getString("sessionId");
                sessionGroups.computeIfAbsent(sessionId, k -> new ArrayList<>()).add(record);
            }

            // Get the most recent session (sessions are timestamp-based, so lexicographically latest is most recent)
            String latestSessionId = sessionGroups.keySet().stream()
                .max(String::compareTo)
                .orElse(null);

            if (latestSessionId != null) {
                // Count students who checked in
                int checkedIn = sessionGroups.get(latestSessionId).size();

                // Total students enrolled (current roster count)
                int totalStudents = classStudents.size();
                int absent = Math.max(0, totalStudents - checkedIn);

                // Update pie chart with previous session data
                updatePieChart(checkedIn, absent, "Previous Session");
            }

        } catch (Exception e) {
            // If error loading previous session, just show empty chart
            updatePieChart(0, 0, "No Data Available");
        }
    }

    /**
     * Updates the pie chart with attendance data
     *
     * @param present Number of students present
     * @param absent Number of students absent
     * @param title Title for the pie chart
     */
    private void updatePieChart(int present, int absent, String title) {
        if (sessionPieChart == null) {
            return;
        }

        sessionPieChart.setTitle(title);

        if (present == 0 && absent == 0) {
            // Empty data - clear chart
            sessionPieChart.setData(FXCollections.observableArrayList());
            return;
        }

        // Create pie chart data
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("Present (" + present + ")", present),
            new PieChart.Data("Absent (" + absent + ")", absent)
        );

        sessionPieChart.setData(pieChartData);

        // Apply custom colors to match label colors
        // Must be done after chart is rendered, so use Platform.runLater
        javafx.application.Platform.runLater(() -> {
            int nodeIndex = 0;
            for (PieChart.Data data : pieChartData) {
                // Style the pie slice
                javafx.scene.Node node = sessionPieChart.lookup(".data" + nodeIndex);
                if (node != null) {
                    String color;
                    if (data.getName().startsWith("Present")) {
                        color = "#6B8E6B"; // Green (matches checked-in label)
                    } else {
                        color = "#9B6B6B"; // Red (matches not-checked-in label)
                    }
                    node.setStyle("-fx-pie-color: " + color + ";");
                }
                nodeIndex++;
            }

            // Style the legend symbols (circles)
            javafx.scene.Node legend = sessionPieChart.lookup(".chart-legend");
            if (legend != null) {
                int symbolIndex = 0;
                for (javafx.scene.Node legendItem : ((javafx.scene.layout.Region) legend).getChildrenUnmodifiable()) {
                    if (legendItem instanceof javafx.scene.control.Label) {
                        javafx.scene.Node symbol = legendItem.lookup(".chart-legend-item-symbol");
                        if (symbol != null && symbolIndex < pieChartData.size()) {
                            String color;
                            if (pieChartData.get(symbolIndex).getName().startsWith("Present")) {
                                color = "#6B8E6B"; // Green
                            } else {
                                color = "#9B6B6B"; // Red
                            }
                            symbol.setStyle("-fx-background-color: " + color + ";");
                        }
                        symbolIndex++;
                    }
                }
            }
        });
    }

    /**
     * Starts an attendance session - turns all student labels red (not checked in)
     */
    @FXML
    public void startSession(ActionEvent event) {
        if (classStudents.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Students",
                "No students are enrolled in this class.");
            return;
        }

        try {
            // Generate unique session ID (timestamp-based)
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            currentSessionId = now.format(formatter);

            // Get check-in base URL from config
            Properties config = ConfigManager.loadConfig();
            String baseUrl = config.getProperty("checkinBaseUrl", "https://wguard.netlify.app");

            // Generate QR code
            BufferedImage qrImage = QRCodeGenerator.generateSessionQRCode(
                selectedClass.getClassId(),
                baseUrl,
                300,
                300
            );

            if (qrImage != null) {
                // Display QR code in ImageView
                Image fxImage = SwingFXUtils.toFXImage(qrImage, null);
                sessionQRCode.setImage(fxImage);

                // Toggle display: show QR code, hide pie chart
                sessionQRCode.setVisible(true);
                sessionPieChart.setVisible(false);
            }

            // Create session in database
            String weatherData = "{}"; // TODO: Add current weather data
            dbManager.createSession(selectedClass.getClassId(), currentSessionId, weatherData);

            // Mark session as active
            sessionActive = true;

            // Turn all visible student labels red (not checked in yet)
            for (Map.Entry<String, Label> entry : studentLabelMap.entrySet()) {
                String studentId = entry.getKey();
                Label label = entry.getValue();

                if (label.isVisible()) {
                    label.setStyle("-fx-background-color: #9B6B6B;"); // Red
                    studentStatusMap.put(studentId, "red");
                }
            }

            // Toggle buttons: hide Start, show End
            startSessionButton.setVisible(false);
            startSessionButton.setDisable(true);
            endSessionButton.setVisible(true);
            endSessionButton.setDisable(false);

            // Start polling for attendance updates (every 2 seconds)
            startAttendancePolling();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Session Error",
                "Failed to start session: " + e.getMessage());
        }
    }

    /**
     * Starts polling the database for new attendance records
     */
    private void startAttendancePolling() {
        // Poll every 2 seconds
        attendancePollingTimer = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            checkForNewAttendance();
        }));
        attendancePollingTimer.setCycleCount(Timeline.INDEFINITE);
        attendancePollingTimer.play();
    }

    /**
     * Stops the attendance polling timer
     */
    private void stopAttendancePolling() {
        if (attendancePollingTimer != null) {
            attendancePollingTimer.stop();
            attendancePollingTimer = null;
        }
    }

    /**
     * Checks database for new attendance records and updates labels
     */
    private void checkForNewAttendance() {
        if (!sessionActive || currentSessionId == null) {
            return;
        }

        // Get all attendance records for this session
        List<Document> attendanceRecords = dbManager.getAttendanceBySession(currentSessionId);

        // Update labels for students who have checked in
        for (Document record : attendanceRecords) {
            String studentId = record.getString("studentId");
            String status = studentStatusMap.get(studentId);

            // Only update if label exists and hasn't been marked green yet
            if (status != null && !status.equals("green")) {
                markStudentCheckedIn(studentId);
            }
        }
    }

    /**
     * Marks a student as checked in - turns label green
     * This method is called when a student checks in via QR code
     *
     * @param studentId The student ID who checked in
     */
    private void markStudentCheckedIn(String studentId) {
        Label label = studentLabelMap.get(studentId);

        if (label != null && sessionActive) {
            label.setStyle("-fx-background-color: #6B8E6B;"); // Green
            studentStatusMap.put(studentId, "green");
        }
    }

    /**
     * Ends the attendance session
     */
    @FXML
    public void endSession(ActionEvent event) {
        if (!sessionActive) {
            showAlert(Alert.AlertType.WARNING, "No Active Session",
                "No session is currently active.");
            return;
        }

        // Stop polling for attendance updates
        stopAttendancePolling();

        // Close session in database
        if (currentSessionId != null) {
            dbManager.closeSession(currentSessionId);
        }

        // Mark session as inactive
        sessionActive = false;

        // Count attendance statistics
        int totalStudents = studentLabelMap.size();
        long checkedIn = studentStatusMap.values().stream()
            .filter(status -> status.equals("green"))
            .count();
        long absent = totalStudents - checkedIn;

        // Update pie chart with current session results
        updatePieChart((int) checkedIn, (int) absent, "Current Session Results");

        // Toggle display: hide QR code, show pie chart
        sessionQRCode.setVisible(false);
        sessionPieChart.setVisible(true);

        // Reset all labels back to gray
        for (Map.Entry<String, Label> entry : studentLabelMap.entrySet()) {
            String studentId = entry.getKey();
            Label label = entry.getValue();

            if (label.isVisible()) {
                label.setStyle("-fx-background-color: #808080;"); // Gray
                studentStatusMap.put(studentId, "gray");
            }
        }

        // Clear QR code image
        sessionQRCode.setImage(null);

        // Reset session ID
        currentSessionId = null;

        // Toggle buttons: show Start, hide End
        startSessionButton.setVisible(true);
        startSessionButton.setDisable(false);
        endSessionButton.setVisible(false);
        endSessionButton.setDisable(true);

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

                // Pass forecast data WITH temperature unit preference (from WeatherService) and ClassInfo
                boolean isFahrenheit = weatherService.isUsingFahrenheit();
                FiveDayForecastController.setCurrentForecast(forecast, cityName, country, isFahrenheit, selectedClass);
            }

            // Load FiveDayForecastView
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/FiveDayForecastView.fxml"));
            Stage stage = (Stage) fiveDayForecastButton2.getScene().getWindow();
            Scene scene = new Scene(root); // Let it use FXML's preferred size (600x350)
            stage.setScene(scene);
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            // e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                "Failed to load 5-Day Forecast view: " + e.getMessage());
        }
    }

    @FXML
    public void toggleTemperatureUnit(ActionEvent event) {
        // Toggle temperature unit preference in WeatherService facade
        if (weatherService != null) {
            boolean currentUnit = weatherService.isUsingFahrenheit();
            weatherService.setTemperatureUnit(!currentUnit);

            // Update the temperature display
            updateTemperatureDisplay();
        }
    }

    @FXML
    public void switchToAdminView(ActionEvent event) {
        try {
            // Stop polling timer if session is active
            if (sessionActive) {
                stopAttendancePolling();
            }

            // Load AdminView FXML
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AdminView.fxml"));
            Stage stage = (Stage) adminViewButton.getScene().getWindow();

            // Set scene with proper AdminView size (350x520 from FXML)
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Force resize to AdminView dimensions
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                "Failed to load Admin View: " + e.getMessage());
        }
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
