package com.weatherboys;

/**
 * Launcher class for JavaFX application
 * This separate launcher is required to avoid JavaFX module issues
 * when running via Gradle or from a JAR file
 */
public class Launcher {
    public static void main(String[] args) {
        Main.main(args);
    }
}
