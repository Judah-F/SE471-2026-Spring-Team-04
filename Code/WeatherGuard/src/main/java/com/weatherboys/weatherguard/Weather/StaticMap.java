package com.weatherboys.weatherguard.Weather;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URLConnection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StaticMap {

    private static final Logger logger = Logger.getLogger(StaticMap.class.getName());
    private BufferedImage mapImage;
    private double lat;
    private double lon;

    public StaticMap() {
    }

    public StaticMap(BufferedImage mapImage, double lat, double lon) {
        this.mapImage = mapImage;
        this.lat = lat;
        this.lon = lon;
    }

    // Fetch map for a city location with multiple weather layers
    public static StaticMap fetchMapForLocation(double lat, double lon) {
        String apiKey = null;
        try {
            Properties config = ConfigManager.loadConfig();
            apiKey = config.getProperty("apiKey");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to load API key from config", e);
        }

        if (apiKey == null || apiKey.trim().isEmpty()) {
            logger.log(Level.SEVERE, "API key is missing or empty in config");
            return new StaticMap();
        }

        // Calculate tile coordinates (zoom 10 = city level view)
        int zoom = 5;
        int x = (int) Math.floor((lon + 180.0) / 360.0 * (1 << zoom));
        int y = (int) Math.floor((1.0 - Math.log(Math.tan(Math.toRadians(lat)) +
                1.0 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2.0 * (1 << zoom));

        try {
            // 1. Fetch base map from OpenStreetMap (with proper User-Agent)
            String osmUrl = String.format(
                    "https://tile.openstreetmap.org/%d/%d/%d.png",
                    zoom, x, y
            );
            URLConnection osmConnection = URI.create(osmUrl).toURL().openConnection();
            osmConnection.setRequestProperty("User-Agent", "WeatherGuard/1.0 (Educational Project)");
            BufferedImage baseMap = ImageIO.read(osmConnection.getInputStream());
//            logger.log(Level.INFO, "Base map fetched from OpenStreetMap");

            // 2. Stack multiple weather layers
            // Order matters: drawn bottom to top
            // For extreme weather monitoring: temperature, precipitation, clouds
            String[] layers = {"temp_new", "precipitation_new", "clouds_new"};

            for (String layer : layers) {
                try {
                    String weatherUrl = String.format(
                            "http://tile.openweathermap.org/map/%s/%d/%d/%d.png?appid=%s",
                            layer, zoom, x, y, apiKey
                    );
                    BufferedImage weatherLayer = ImageIO.read(URI.create(weatherUrl).toURL());

                    // Overlay this weather layer on the base map
                    Graphics2D g = baseMap.createGraphics();
                    g.drawImage(weatherLayer, 0, 0, null);
                    g.dispose();

//                    logger.log(Level.INFO, "Applied " + layer + " layer");
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Failed to fetch " + layer + " layer, continuing with other layers", e);
                    // Continue with remaining layers even if one fails
                }
            }

//            logger.log(Level.INFO, "Combined map with all weather layers created successfully");
            return new StaticMap(baseMap, lat, lon);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to fetch base map", e);
            return new StaticMap();
        }
    }

    public boolean isEmpty() {
        return mapImage == null;
    }

    // Getters
    public BufferedImage getMapImage() { return mapImage; }
    public double getLat() { return lat; }
    public double getLon() { return lon; }
}
