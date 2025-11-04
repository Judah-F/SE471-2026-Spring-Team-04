package com.weatherboys;

import com.weatherboys.weatherguard.Weather.StaticMap;
import com.weatherboys.weatherguard.Weather.Forecast;
import com.weatherboys.weatherguard.Weather.Weather;
import com.weatherboys.weatherguard.Weather.ConfigManager;
import com.weatherboys.weatherguard.WeatherGuard;
import com.weatherboys.weatherguard.QRCodeGenerator;
import com.weatherboys.weatherguard.DatabaseManager;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.Properties;

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

        // Test QR Code Generator
        try {
            testQRCode();
        } catch (Exception e) {
            System.out.println("ERROR in testQRCode:");
            e.printStackTrace();
        }

        // Test MongoDB
        try {
            testMongoDB();
        } catch (Exception e) {
            System.out.println("ERROR in testMongoDB:");
            e.printStackTrace();
        }

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

    private static void testQRCode() {
        System.out.println("testing QRCode...");
        BufferedImage qr = QRCodeGenerator.generateSessionQRCode("BIO101", "https://wguard.netlify.app/");
        JFrame frame = new JFrame("QR Test");
        frame.add(new JLabel(new ImageIcon(qr)));
        frame.pack();
        frame.setVisible(true);
    }

    private static void testMongoDB() throws Exception {
        System.out.println("\n=== UPLOADING CLASS ROSTERS ===");

        // Load config
        Properties config = ConfigManager.loadConfig();
        String connectionString = config.getProperty("mongoConnectionString");
        String databaseName = config.getProperty("mongoDatabaseName");

        if (connectionString == null || connectionString.isEmpty()) {
            System.out.println("MongoDB connection string not found in config.properties");
            return;
        }

        // Create DatabaseManager
        DatabaseManager db = new DatabaseManager(connectionString, databaseName);

        // Upload BIO101 roster
        System.out.println("\nUploading BIO101 roster...");
        boolean bio101Uploaded = db.uploadRosterCsv("resources/BIO101_Roster.csv");
        if (bio101Uploaded) {
            System.out.println("✓ BIO101 roster uploaded successfully!");
        } else {
            System.out.println("✗ Failed to upload BIO101 roster");
        }

        // Upload CHEM201 roster
        System.out.println("\nUploading CHEM201 roster...");
        boolean chem201Uploaded = db.uploadRosterCsv("resources/CHEM201_Roster.csv");
        if (chem201Uploaded) {
            System.out.println("✓ CHEM201 roster uploaded successfully!");
        } else {
            System.out.println("✗ Failed to upload CHEM201 roster");
        }

        System.out.println("\n=== GENERATING QR CODES FOR CLASSES ===");

        // Generate QR codes for each class
        String baseUrl = "https://wguard.netlify.app";

        // BIO101 QR Code
        String bio101SessionId = "20251025_150000";
        BufferedImage bio101QR = QRCodeGenerator.generateSessionQRCode("BIO101", baseUrl);
        if (bio101QR != null) {
            System.out.println("\n✓ BIO101 QR Code generated!");
            System.out.println("  URL: " + baseUrl + "?classId=BIO101&sessionId=" + bio101SessionId);
            System.out.println("  Valid Students: S001 (John Smith), S002 (Jane Doe), S003 (Alice Johnson), S004 (Bob Williams), S005 (Charlie Brown)");

            // Create session in DB
            db.createSession("BIO101", bio101SessionId, "{\"temp\":32,\"condition\":\"Snow\"}");

            // Save QR code image
            QRCodeGenerator.saveQRCodeImage(bio101QR, "BIO101_QR.png");
            System.out.println("  QR Code saved to: BIO101_QR.png");
        }

        // CHEM201 QR Code
        String chem201SessionId = "20251025_150100";
        BufferedImage chem201QR = QRCodeGenerator.generateSessionQRCode("CHEM201", baseUrl);
        if (chem201QR != null) {
            System.out.println("\n✓ CHEM201 QR Code generated!");
            System.out.println("  URL: " + baseUrl + "?classId=CHEM201&sessionId=" + chem201SessionId);
            System.out.println("  Valid Students: S006 (Emma Davis), S007 (Oliver Garcia), S008 (Sophia Martinez), S009 (Liam Rodriguez), S010 (Ava Wilson)");

            // Create session in DB
            db.createSession("CHEM201", chem201SessionId, "{\"temp\":28,\"condition\":\"Clear\"}");

            // Save QR code image
            QRCodeGenerator.saveQRCodeImage(chem201QR, "CHEM201_QR.png");
            System.out.println("  QR Code saved to: CHEM201_QR.png");
        }

        System.out.println("\n=== TEST SCENARIOS ===");
        System.out.println("\nTo test SUCCESSFUL check-in:");
        System.out.println("  1. Open: " + baseUrl + "?classId=BIO101&sessionId=" + bio101SessionId);
        System.out.println("  2. Enter: StudentID=S001, Name=John Smith");
        System.out.println("  3. Should see: ✅ Check-in successful!");

        System.out.println("\nTo test FAILED check-in (wrong class):");
        System.out.println("  1. Open: " + baseUrl + "?classId=BIO101&sessionId=" + bio101SessionId);
        System.out.println("  2. Enter: StudentID=S006, Name=Emma Davis (CHEM201 student)");
        System.out.println("  3. Should see: ❌ You are not enrolled in this class");

        System.out.println("\nTo test FAILED check-in (unknown student):");
        System.out.println("  1. Open: " + baseUrl + "?classId=BIO101&sessionId=" + bio101SessionId);
        System.out.println("  2. Enter: StudentID=S999, Name=Test Student");
        System.out.println("  3. Should see: ❌ Student ID not found");

        // Close connection
        db.close();
        System.out.println("\n=== Setup Complete! ===\n");
    }
}
