const { MongoClient } = require('mongodb');

// MongoDB connection URI - stored in Netlify environment variables
const MONGO_URI = process.env.MONGO_CONNECTION_STRING;
const DB_NAME = process.env.MONGO_DATABASE_NAME || 'weatherguard';

let cachedClient = null;

async function connectToDatabase() {
    if (cachedClient) {
        return cachedClient;
    }

    const client = new MongoClient(MONGO_URI);
    await client.connect();
    cachedClient = client;
    return client;
}

exports.handler = async (event, context) => {
    // Only allow POST requests
    if (event.httpMethod !== 'POST') {
        return {
            statusCode: 405,
            body: JSON.stringify({ error: 'Method not allowed' })
        };
    }

    try {
        // Parse request body
        const { classId, sessionId, studentId, studentName } = JSON.parse(event.body);

        // Validate input
        if (!classId || !sessionId || !studentId || !studentName) {
            return {
                statusCode: 400,
                body: JSON.stringify({ error: 'Missing required fields' })
            };
        }

        // Connect to MongoDB
        const client = await connectToDatabase();
        const db = client.db(DB_NAME);
        const studentsCollection = db.collection('students');
        const attendanceCollection = db.collection('attendance');

        // VALIDATE: Check if student exists and is enrolled in this class
        const student = await studentsCollection.findOne({
            studentId: studentId,
            classId: classId
        });

        if (!student) {
            // Check if student exists at all
            const anyStudent = await studentsCollection.findOne({ studentId: studentId });

            if (!anyStudent) {
                return {
                    statusCode: 404,
                    body: JSON.stringify({
                        error: 'Student ID not found in system',
                        invalid: true
                    })
                };
            } else {
                return {
                    statusCode: 403,
                    body: JSON.stringify({
                        error: 'You are not enrolled in this class',
                        wrongClass: true
                    })
                };
            }
        }

        // VALIDATE: Check if name matches roster
        if (student.studentName.toLowerCase() !== studentName.toLowerCase()) {
            return {
                statusCode: 400,
                body: JSON.stringify({
                    error: 'Name does not match Student ID on roster',
                    nameMismatch: true,
                    expectedName: student.studentName
                })
            };
        }

        // Check for duplicate check-in
        const existing = await attendanceCollection.findOne({
            sessionId: sessionId,
            studentId: studentId
        });

        if (existing) {
            return {
                statusCode: 409,
                body: JSON.stringify({
                    error: 'You have already checked in for this session',
                    duplicate: true
                })
            };
        }

        // Create attendance record
        const attendanceRecord = {
            classId: classId,
            sessionId: sessionId,
            studentId: studentId,
            studentName: student.studentName, // Use name from roster
            checkInTime: new Date().toISOString(),
            status: 'present'
        };

        await attendanceCollection.insertOne(attendanceRecord);

        return {
            statusCode: 200,
            body: JSON.stringify({
                success: true,
                message: 'Attendance recorded successfully',
                studentName: student.studentName
            })
        };

    } catch (error) {
        console.error('Error processing check-in:', error);
        return {
            statusCode: 500,
            body: JSON.stringify({
                error: 'Internal server error',
                message: error.message
            })
        };
    }
};
