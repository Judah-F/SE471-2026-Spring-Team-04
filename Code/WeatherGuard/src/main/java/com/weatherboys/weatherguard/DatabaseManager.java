package com.weatherboys.weatherguard;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.weatherboys.weatherguard.Weather.ConfigManager;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DatabaseManager handles all MongoDB operations for WeatherGuard attendance system.
 * Manages connections, sessions, and attendance records in MongoDB.
 * Implements Singleton pattern for shared database access across controllers.
 */
public class DatabaseManager {

    private static final Logger logger = Logger.getLogger(DatabaseManager.class.getName());
    private static DatabaseManager instance = null;

    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> sessionsCollection;
    private final MongoCollection<Document> attendanceCollection;
    private final MongoCollection<Document> classesCollection;
    private final MongoCollection<Document> studentsCollection;

    /**
     * Creates a new DatabaseManager and connects to MongoDB.
     *
     * @param connectionString MongoDB connection string (e.g., "mongodb://localhost:27017" or MongoDB Atlas URI)
     * @param databaseName Name of the database to use
     */
    public DatabaseManager(String connectionString, String databaseName) {
        this.mongoClient = MongoClients.create(connectionString);
        this.database = mongoClient.getDatabase(databaseName);
        this.sessionsCollection = database.getCollection("sessions");
        this.attendanceCollection = database.getCollection("attendance");
        this.classesCollection = database.getCollection("classes");
        this.studentsCollection = database.getCollection("students");
    }

    /**
     * Gets the singleton instance of DatabaseManager.
     * Loads configuration from config.properties and creates instance if needed.
     *
     * @return The DatabaseManager singleton instance
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            try {
                Properties config = ConfigManager.loadConfig();
                String connectionString = config.getProperty("mongoConnectionString");
                String databaseName = config.getProperty("mongoDatabaseName");

                if (connectionString == null || databaseName == null) {
                    logger.log(Level.SEVERE, "MongoDB configuration missing in config.properties");
                    throw new RuntimeException("MongoDB configuration not found");
                }

                instance = new DatabaseManager(connectionString, databaseName);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Failed to load configuration", e);
                throw new RuntimeException("Failed to initialize DatabaseManager", e);
            }
        }
        return instance;
    }

    /**
     * Gets all classes from the database.
     *
     * @return List of all class documents
     */
    public List<Document> getAllClasses() {
        List<Document> classes = new ArrayList<>();
        try {
            classesCollection.find().into(classes);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to retrieve classes", e);
        }
        return classes;
    }

    /**
     * Gets only active classes from the database (soft delete support).
     *
     * @return List of active class documents
     */
    public List<Document> getActiveClasses() {
        List<Document> classes = new ArrayList<>();
        try {
            classesCollection.find(Filters.eq("active", true)).into(classes);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to retrieve active classes", e);
        }
        return classes;
    }

    /**
     * Creates a new attendance session in the database.
     *
     * @param classId The class identifier (e.g., "BIO101")
     * @param sessionId Unique session ID (timestamp-based from QR code)
     * @param weatherData Current weather data as JSON string
     * @return The MongoDB ObjectId of the created session, or null if failed
     */
    public String createSession(String classId, String sessionId, String weatherData) {
        try {
            Document session = new Document("classId", classId)
                    .append("sessionId", sessionId)
                    .append("createdAt", LocalDateTime.now().toString())
                    .append("weatherData", weatherData)
                    .append("active", true);

            sessionsCollection.insertOne(session);
            logger.log(Level.INFO, "Session created: " + sessionId + " for class: " + classId);

            return session.getObjectId("_id").toString();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to create session: " + sessionId, e);
            return null;
        }
    }

    /**
     * Records a student attendance check-in.
     *
     * @param classId The class identifier
     * @param sessionId The session identifier
     * @param studentId Student's unique ID
     * @param studentName Student's name
     * @return true if successful, false otherwise
     */
    public boolean markAttendance(String classId, String sessionId, String studentId, String studentName) {
        try {
            // Check for duplicate check-in
            Document existing = attendanceCollection.find(
                Filters.and(
                    Filters.eq("sessionId", sessionId),
                    Filters.eq("studentId", studentId)
                )
            ).first();

            if (existing != null) {
                logger.log(Level.WARNING, "Duplicate check-in attempt: " + studentId + " in session " + sessionId);
                return false;
            }

            // Create attendance record
            Document attendance = new Document("classId", classId)
                    .append("sessionId", sessionId)
                    .append("studentId", studentId)
                    .append("studentName", studentName)
                    .append("checkInTime", LocalDateTime.now().toString())
                    .append("status", "present");

            attendanceCollection.insertOne(attendance);
            logger.log(Level.INFO, "Attendance marked: " + studentName + " (" + studentId + ")");

            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to mark attendance for: " + studentId, e);
            return false;
        }
    }

    /**
     * Retrieves all attendance records for a specific session.
     *
     * @param sessionId The session identifier
     * @return List of attendance documents
     */
    public List<Document> getAttendanceBySession(String sessionId) {
        List<Document> records = new ArrayList<>();
        try {
            attendanceCollection.find(Filters.eq("sessionId", sessionId))
                    .into(records);
            logger.log(Level.INFO, "Retrieved " + records.size() + " attendance records for session: " + sessionId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to retrieve attendance for session: " + sessionId, e);
        }
        return records;
    }

    /**
     * Retrieves all attendance records for a specific class.
     *
     * @param classId The class identifier
     * @return List of attendance documents
     */
    public List<Document> getAttendanceByClass(String classId) {
        List<Document> records = new ArrayList<>();
        try {
            attendanceCollection.find(Filters.eq("classId", classId))
                    .into(records);
            logger.log(Level.INFO, "Retrieved " + records.size() + " attendance records for class: " + classId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to retrieve attendance for class: " + classId, e);
        }
        return records;
    }

    /**
     * Retrieves session information by session ID.
     *
     * @param sessionId The session identifier
     * @return Session document, or null if not found
     */
    public Document getSession(String sessionId) {
        try {
            Document session = sessionsCollection.find(Filters.eq("sessionId", sessionId)).first();
            if (session != null) {
                logger.log(Level.INFO, "Retrieved session: " + sessionId);
            } else {
                logger.log(Level.WARNING, "Session not found: " + sessionId);
            }
            return session;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to retrieve session: " + sessionId, e);
            return null;
        }
    }

    /**
     * Closes the session (marks it as inactive).
     *
     * @param sessionId The session identifier
     * @return true if successful, false otherwise
     */
    public boolean closeSession(String sessionId) {
        try {
            sessionsCollection.updateOne(
                Filters.eq("sessionId", sessionId),
                Updates.set("active", false)
            );
            logger.log(Level.INFO, "Session closed: " + sessionId);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to close session: " + sessionId, e);
            return false;
        }
    }

    /**
     * Gets all active sessions.
     *
     * @return List of active session documents
     */
    public List<Document> getActiveSessions() {
        List<Document> sessions = new ArrayList<>();
        try {
            sessionsCollection.find(Filters.eq("active", true))
                    .into(sessions);
            logger.log(Level.INFO, "Retrieved " + sessions.size() + " active sessions");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to retrieve active sessions", e);
        }
        return sessions;
    }

    /**
     * Uploads a class roster from CSV file and stores in MongoDB.
     * CSV Format:
     * - First 8 rows: Class metadata (ClassName, ClassID, Semester, Year, StartDate, EndDate, ProfessorName, City)
     * - Blank row
     * - Header row: StudentName,StudentID
     * - Student rows: Name,ID
     *
     * @param filePath Path to the roster CSV file
     * @return true if successful, false otherwise
     */
    public boolean uploadRosterCsv(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Parse class metadata (first 8 lines)
            String className = br.readLine().split(",", 2)[1].trim();
            String classId = br.readLine().split(",", 2)[1].trim();
            String semester = br.readLine().split(",", 2)[1].trim();
            int year = Integer.parseInt(br.readLine().split(",", 2)[1].trim());
            String startDate = br.readLine().split(",", 2)[1].trim();
            String endDate = br.readLine().split(",", 2)[1].trim();
            String professorName = br.readLine().split(",", 2)[1].trim();
            String city = br.readLine().split(",", 2)[1].trim();

            // Create class document with active flag
            Document classDoc = new Document("classId", classId)
                    .append("className", className)
                    .append("semester", semester)
                    .append("year", year)
                    .append("startDate", startDate)
                    .append("endDate", endDate)
                    .append("professorName", professorName)
                    .append("city", city)
                    .append("active", true);

            // Check if class already exists
            Document existing = classesCollection.find(Filters.eq("classId", classId)).first();
            if (existing != null) {
                // Update existing class document and set active = true
                classesCollection.replaceOne(Filters.eq("classId", classId), classDoc);

                // Delete old students to replace with new roster
                studentsCollection.deleteMany(Filters.eq("classId", classId));
            } else {
                // Insert new class
                classesCollection.insertOne(classDoc);
            }

            // Skip blank line and header
            br.readLine(); // blank line
            br.readLine(); // "StudentName,StudentID" header

            // Parse students
            String line;
            int studentCount = 0;
            while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String studentName = parts[0].trim();
                    String studentId = parts[1].trim();

                    Document studentDoc = new Document("studentId", studentId)
                            .append("studentName", studentName)
                            .append("classId", classId);

                    studentsCollection.insertOne(studentDoc);
                    studentCount++;
                }
            }

            return true;

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to upload roster from: " + filePath, e);
            return false;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing roster CSV: " + filePath, e);
            return false;
        }
    }

    /**
     * Gets all students enrolled in a specific class.
     *
     * @param classId The class identifier
     * @return List of student documents
     */
    public List<Document> getStudentsByClass(String classId) {
        List<Document> students = new ArrayList<>();
        try {
            studentsCollection.find(Filters.eq("classId", classId))
                    .into(students);
            logger.log(Level.INFO, "Retrieved " + students.size() + " students for class: " + classId);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to retrieve students for class: " + classId, e);
        }
        return students;
    }

    /**
     * Validates if a student is enrolled in a specific class.
     *
     * @param studentId Student ID to validate
     * @param classId Class ID to check enrollment
     * @return Student document if enrolled, null if not found or not enrolled
     */
    public Document validateStudent(String studentId, String classId) {
        try {
            Document student = studentsCollection.find(
                Filters.and(
                    Filters.eq("studentId", studentId),
                    Filters.eq("classId", classId)
                )
            ).first();

            if (student != null) {
                logger.log(Level.INFO, "Student validated: " + studentId + " in class: " + classId);
            } else {
                logger.log(Level.WARNING, "Student validation failed: " + studentId + " not enrolled in: " + classId);
            }

            return student;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error validating student: " + studentId, e);
            return null;
        }
    }

    /**
     * Gets class information by class ID.
     *
     * @param classId The class identifier
     * @return Class document, or null if not found
     */
    public Document getClassInfo(String classId) {
        try {
            Document classDoc = classesCollection.find(Filters.eq("classId", classId)).first();
            if (classDoc != null) {
                logger.log(Level.INFO, "Retrieved class info: " + classId);
            } else {
                logger.log(Level.WARNING, "Class not found: " + classId);
            }
            return classDoc;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error retrieving class: " + classId, e);
            return null;
        }
    }

    /**
     * Soft deletes a class by setting its active flag to false.
     * This preserves historical data while hiding the class from active views.
     * Students enrolled in the class remain in the database for historical reporting.
     *
     * @param classId The class identifier to soft delete
     * @return true if successful, false otherwise
     */
    public boolean deleteClass(String classId) {
        try {
            // Set active = false instead of actually deleting
            long modifiedCount = classesCollection.updateOne(
                Filters.eq("classId", classId),
                Updates.set("active", false)
            ).getModifiedCount();

            if (modifiedCount > 0) {
                return true;
            } else {
                logger.log(Level.WARNING, "Class not found for deletion: " + classId);
                return false;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to delete class: " + classId, e);
            return false;
        }
    }

    /**
     * Closes the MongoDB connection.
     * Should be called when shutting down the application.
     */
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
