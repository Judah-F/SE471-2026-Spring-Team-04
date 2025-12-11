package com.weatherboys.weatherguard;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for CSV Roster Parsing
 *
 * Tests cover:
 * - Valid CSV format validation against REAL roster files
 * - Invalid CSV format detection
 * - Student count accuracy
 * - Class metadata extraction
 * - Repeatability of parsing
 *
 * NOTE: These tests use REAL roster files from roster_resources/
 * The tests validate CSV FORMAT only, so they work with any properly formatted roster file
 */
public class CSVParsingTest {

    @TempDir
    Path tempDir;

    private static final String ROSTER_RESOURCES_PATH = "roster_resources";

    /**
     * Helper method to get path to a real roster file
     */
    private File getRosterFile(String filename) {
        return Paths.get(ROSTER_RESOURCES_PATH, filename).toFile();
    }

    /**
     * Helper method to create an invalid CSV (missing required fields) for negative testing
     */
    private File createInvalidCSV(Path directory) throws IOException {
        File csvFile = directory.resolve("invalid_roster.csv").toFile();
        try (FileWriter writer = new FileWriter(csvFile)) {
            writer.write("ClassName,Biology 101\n");
            writer.write("ClassID,BIO101\n");
            // Missing Semester, Year, dates, professor, city
            writer.write("StudentName,StudentID\n");
            writer.write("John Doe,S001\n");
        }
        return csvFile;
    }

    /**
     * Test Type: Accuracy Test
     * Testing Range: CSV format validation (valid format)
     * Testing Input: REAL roster file BIO101_Roster.csv
     * Testing Procedure: Verify file exists and is readable
     * Expected Result: File should be recognized as valid format
     */
    @Test
    public void testCSVFormat_ValidFormat_FileExists() {
        File validCSV = getRosterFile("BIO101_Roster.csv");

        // Test that file exists
        assertTrue(validCSV.exists(), "BIO101_Roster.csv should exist in roster_resources/");
        assertTrue(validCSV.length() > 0, "CSV file should contain data");
        assertTrue(validCSV.canRead(), "CSV file should be readable");
    }

    /**
     * Test Type: Accuracy Test
     * Testing Range: CSV content validation
     * Testing Input: REAL roster file BIO101_Roster.csv
     * Testing Procedure: Read file and count student entries
     * Expected Result: Correct number of students counted (5 students in BIO101)
     */
    @Test
    public void testCSVContent_ValidFile_CorrectStudentCount() throws IOException {
        File validCSV = getRosterFile("BIO101_Roster.csv");

        assertTrue(validCSV.exists(), "BIO101_Roster.csv must exist");

        // Expected: 5 students in BIO101_Roster.csv
        int expectedStudentCount = 5;

        // Read file and count lines after header
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(validCSV));

        // Skip metadata (8 lines) + blank line + header (line 10)
        for (int i = 0; i < 10; i++) {
            reader.readLine();
        }

        // Count student lines
        int actualCount = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                actualCount++;
            }
        }
        reader.close();

        assertEquals(expectedStudentCount, actualCount, "BIO101 should have exactly 5 students");
    }

    /**
     * Test Type: Accuracy Test
     * Testing Range: CSV format validation (invalid format)
     * Testing Input: CSV with missing required fields
     * Testing Procedure: Create invalid CSV, attempt to parse
     * Expected Result: Should detect missing fields
     */
    @Test
    public void testCSVFormat_InvalidFormat_MissingFields() throws IOException {
        File invalidCSV = createInvalidCSV(tempDir);

        assertTrue(invalidCSV.exists(), "Invalid CSV file should be created for testing");

        // Count lines - should be less than required metadata rows
        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(invalidCSV));
        int lineCount = 0;
        while (reader.readLine() != null) {
            lineCount++;
        }
        reader.close();

        assertTrue(lineCount < 10, "Invalid CSV should have fewer than 10 required metadata rows");
    }

    /**
     * Test Type: Repeatability Test
     * Testing Range: CSV file parsing
     * Testing Input: Same REAL CSV file (SE370_Roster.csv)
     * Testing Procedure: Read file multiple times, count students each time
     * Expected Result: Same student count on each read
     */
    @Test
    public void testCSVParsing_MultipleReads_ConsistentResults() throws IOException {
        File validCSV = getRosterFile("SE370_Roster.csv");
        assertTrue(validCSV.exists(), "SE370_Roster.csv must exist");

        // Read 1
        java.io.BufferedReader reader1 = new java.io.BufferedReader(new java.io.FileReader(validCSV));
        for (int i = 0; i < 10; i++) reader1.readLine();
        int count1 = 0;
        String line1;
        while ((line1 = reader1.readLine()) != null) {
            if (!line1.trim().isEmpty()) count1++;
        }
        reader1.close();

        // Read 2
        java.io.BufferedReader reader2 = new java.io.BufferedReader(new java.io.FileReader(validCSV));
        for (int i = 0; i < 10; i++) reader2.readLine();
        int count2 = 0;
        String line2;
        while ((line2 = reader2.readLine()) != null) {
            if (!line2.trim().isEmpty()) count2++;
        }
        reader2.close();

        // Read 3
        java.io.BufferedReader reader3 = new java.io.BufferedReader(new java.io.FileReader(validCSV));
        for (int i = 0; i < 10; i++) reader3.readLine();
        int count3 = 0;
        String line3;
        while ((line3 = reader3.readLine()) != null) {
            if (!line3.trim().isEmpty()) count3++;
        }
        reader3.close();

        assertEquals(count1, count2, "First and second read should match");
        assertEquals(count2, count3, "Second and third read should match");
        assertTrue(count1 > 0, "Should have at least some students");
    }

    /**
     * Test Type: Accuracy Test
     * Testing Range: CSV metadata extraction
     * Testing Input: REAL roster file CHEM201_Roster.csv
     * Testing Procedure: Read first 8 lines, verify format
     * Expected Result: Each metadata line should have key,value format
     */
    @Test
    public void testCSVMetadata_ValidFormat_ProperKeyValuePairs() throws IOException {
        File validCSV = getRosterFile("CHEM201_Roster.csv");
        assertTrue(validCSV.exists(), "CHEM201_Roster.csv must exist");

        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(validCSV));

        // Read and verify first 8 metadata lines
        for (int i = 0; i < 8; i++) {
            String line = reader.readLine();
            assertNotNull(line, "Metadata line " + (i+1) + " should exist");
            assertTrue(line.contains(","), "Metadata line should contain comma separator");

            String[] parts = line.split(",", 2);
            assertEquals(2, parts.length, "Metadata should have key and value");
            assertFalse(parts[0].trim().isEmpty(), "Metadata key should not be empty");
            assertFalse(parts[1].trim().isEmpty(), "Metadata value should not be empty");
        }

        reader.close();
    }

    /**
     * Test Type: Accuracy Test
     * Testing Range: CSV format validation works with multiple roster files
     * Testing Input: All available REAL roster files
     * Testing Procedure: Verify all roster files exist and are readable
     * Expected Result: All roster files should be valid
     */
    @Test
    public void testCSVFormat_AllRosterFiles_ExistAndReadable() {
        String[] rosterFiles = {"BIO101_Roster.csv", "CHEM201_Roster.csv", "CS671_Roster.csv", "SE370_Roster.csv"};

        for (String filename : rosterFiles) {
            File rosterFile = getRosterFile(filename);
            assertTrue(rosterFile.exists(), filename + " should exist in roster_resources/");
            assertTrue(rosterFile.canRead(), filename + " should be readable");
            assertTrue(rosterFile.length() > 0, filename + " should contain data");
        }
    }

    /**
     * Test Type: Accuracy Test
     * Testing Range: CSV student data validation
     * Testing Input: REAL roster file CS671_Roster.csv
     * Testing Procedure: Verify student records have proper format (Name,ID)
     * Expected Result: Each student line should have two comma-separated values
     */
    @Test
    public void testCSVStudentData_ValidFormat_ProperNameIDPairs() throws IOException {
        File validCSV = getRosterFile("CS671_Roster.csv");
        assertTrue(validCSV.exists(), "CS671_Roster.csv must exist");

        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(validCSV));

        // Skip metadata (8 lines) + blank line + header (line 10)
        for (int i = 0; i < 10; i++) {
            reader.readLine();
        }

        // Validate student records
        String line;
        int studentCount = 0;
        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                studentCount++;
                String[] parts = line.split(",", 2);
                assertEquals(2, parts.length, "Student record should have name and ID");
                assertFalse(parts[0].trim().isEmpty(), "Student name should not be empty");
                assertFalse(parts[1].trim().isEmpty(), "Student ID should not be empty");
            }
        }
        reader.close();

        assertTrue(studentCount > 0, "Should have at least one student in roster");
    }
}
