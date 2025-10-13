package com.weatherboys.weatherguard.Weather;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigManager {
	// Production file management
	/*
    private static final String OS = System.getProperty("os.name").toLowerCase();
    private static final String CONFIG_FILE_PATH;

    static {
        if (OS.contains("win")) {
            CONFIG_FILE_PATH = Paths.get(System.getProperty("user.dir"), "config.properties").toString();
        } else if (OS.contains("mac")) {
            CONFIG_FILE_PATH = Paths.get(System.getProperty("user.home"), "Library", "Application Support", "JavaWeather", "config.properties").toString();
        } else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) {
            CONFIG_FILE_PATH = Paths.get(System.getProperty("user.home"), ".config", "JavaWeather", "config.properties").toString();
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: " + OS);
        }
    }
	*/
	
	// Dev file management
	private static final String CONFIG_FILE_PATH = "config.properties";

    public static void saveConfig(Properties properties) throws IOException {
        Path configFile = Paths.get(CONFIG_FILE_PATH);

        if (!Files.exists(configFile.getParent())) {
            Files.createDirectories(configFile.getParent());
        }

        try (var outputStream = Files.newOutputStream(configFile)) {
            properties.store(outputStream, null);
        }
    }

    public static Properties loadConfig() throws IOException {
        Path configFile = Paths.get(CONFIG_FILE_PATH);

        Properties properties = new Properties();
        if (Files.exists(configFile)) {
            try (var inputStream = Files.newInputStream(configFile)) {
                properties.load(inputStream);
            }
        }
        return properties;
    }

    public static void ensureConfigDirectoryExists() throws IOException {
        Path configFile = Paths.get(CONFIG_FILE_PATH);
        if (configFile.getParent() != null && !Files.exists(configFile.getParent())) {
            Files.createDirectories(configFile.getParent());
        }
    }
}

