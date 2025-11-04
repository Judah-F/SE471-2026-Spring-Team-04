# Test Resources

Sample class roster CSV files for testing WeatherGuard functionality.

## Class Roster Format

Each class has ONE CSV file containing:
1. **Class metadata** (first 7 rows)
2. **Student roster** (remaining rows)

### Example: BIO101_Roster.csv
```csv
ClassName,Biology 101
ClassID,BIO101
Semester,Fall
Year,2025
StartDate,2025-08-25
EndDate,2025-12-15
ProfessorName,Dr. Anderson

StudentName,StudentID
John Smith,S001
Jane Doe,S002
...
```

## Available Test Files

### BIO101_Roster.csv
- **Class**: Biology 101
- **Professor**: Dr. Anderson
- **Semester**: Fall 2025
- **Students**: 5 students (S001-S005)

### CHEM201_Roster.csv
- **Class**: Chemistry 201
- **Professor**: Prof. Johnson
- **Semester**: Fall 2025
- **Students**: 5 students (S006-S010)

## Usage

### Development/Testing (Current):
1. Manually upload roster CSV to MongoDB
2. Test check-ins with known StudentIDs (S001-S010)
3. Validate against roster

### Production (Future):
1. Admin clicks "Upload Roster" in UI
2. Selects CSV file (e.g., `BIO101_Roster.csv`)
3. System parses file and creates:
   - Class record in `classes` collection
   - Student records in `students` collection (linked to ClassID)
4. Students can only check in if their StudentID is in the roster

## Test Data Summary

**BIO101 Students:**
- S001 - John Smith
- S002 - Jane Doe
- S003 - Alice Johnson
- S004 - Bob Williams
- S005 - Charlie Brown

**CHEM201 Students:**
- S006 - Emma Davis
- S007 - Oliver Garcia
- S008 - Sophia Martinez
- S009 - Liam Rodriguez
- S010 - Ava Wilson
