package com.weatherboys.weatherguard;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * QRCodeGenerator provides functionality to generate QR codes for class attendance sessions.
 * Uses ZXing library to encode session URLs into QR code images.
 *
 * Each QR code encodes a unique URL containing the class ID and session timestamp,
 * which students scan to access the web-based check-in interface.
 */
public class QRCodeGenerator {

    private static final Logger logger = Logger.getLogger(QRCodeGenerator.class.getName());
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;

    /**
     * Generates a QR code for a class attendance session.
     *
     * @param classId The unique identifier for the class
     * @param baseUrl The base URL of the student check-in portal (e.g., "https://weatherguard.school.edu/checkin")
     * @return BufferedImage containing the QR code, or null if generation fails
     */
    public static BufferedImage generateSessionQRCode(String classId, String baseUrl) {
        return generateSessionQRCode(classId, baseUrl, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Generates a QR code for a class attendance session with custom dimensions.
     *
     * @param classId The unique identifier for the class
     * @param baseUrl The base URL of the student check-in portal
     * @param width Width of the QR code image in pixels
     * @param height Height of the QR code image in pixels
     * @return BufferedImage containing the QR code, or null if generation fails
     */
    public static BufferedImage generateSessionQRCode(String classId, String baseUrl, int width, int height) {
        if (classId == null || classId.trim().isEmpty()) {
            logger.log(Level.WARNING, "Cannot generate QR code: classId is null or empty");
            return null;
        }

        if (baseUrl == null || baseUrl.trim().isEmpty()) {
            logger.log(Level.WARNING, "Cannot generate QR code: baseUrl is null or empty");
            return null;
        }

        // Generate unique session ID using timestamp
        String sessionId = generateSessionId();

        // Build the complete URL that students will be directed to
        String checkInUrl = String.format("%s?classId=%s&sessionId=%s",
            baseUrl.trim(), classId.trim(), sessionId);

        logger.log(Level.INFO, "Generating QR code for URL: " + checkInUrl);

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(checkInUrl, BarcodeFormat.QR_CODE, width, height);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            logger.log(Level.INFO, "QR code generated successfully for class: " + classId);
            return qrImage;

        } catch (WriterException e) {
            logger.log(Level.SEVERE, "Failed to generate QR code for class: " + classId, e);
            return null;
        }
    }

    /**
     * Generates a unique session ID based on current timestamp.
     * Format: YYYYMMDD_HHMMSS
     *
     * @return Session ID string
     */
    private static String generateSessionId() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        return now.format(formatter);
    }

    /**
     * Saves a QR code image to a file.
     *
     * @param qrImage The BufferedImage containing the QR code
     * @param filePath The path where the image should be saved (should end with .png)
     * @return true if save successful, false otherwise
     */
    public static boolean saveQRCodeImage(BufferedImage qrImage, String filePath) {
        if (qrImage == null) {
            logger.log(Level.WARNING, "Cannot save QR code: image is null");
            return false;
        }

        if (filePath == null || filePath.trim().isEmpty()) {
            logger.log(Level.WARNING, "Cannot save QR code: filePath is null or empty");
            return false;
        }

        try {
            File outputFile = new File(filePath);

            // Create parent directories if they don't exist
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            ImageIO.write(qrImage, "PNG", outputFile);
            logger.log(Level.INFO, "QR code saved to: " + filePath);
            return true;

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to save QR code to file: " + filePath, e);
            return false;
        }
    }

    /**
     * Generates and saves a QR code in one operation.
     *
     * @param classId The unique identifier for the class
     * @param baseUrl The base URL of the student check-in portal
     * @param filePath The path where the image should be saved
     * @return true if generation and save successful, false otherwise
     */
    public static boolean generateAndSaveQRCode(String classId, String baseUrl, String filePath) {
        BufferedImage qrImage = generateSessionQRCode(classId, baseUrl);
        if (qrImage == null) {
            return false;
        }
        return saveQRCodeImage(qrImage, filePath);
    }
}
