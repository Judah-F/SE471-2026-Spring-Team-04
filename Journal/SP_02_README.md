# Sprint 2: Database & QR Implementation
**Duration:** 10/13/2025 - 10/26/2025 (2 weeks)
**Sprint Manager:** Josh Clemens

## Sprint Goals
- ✅ Implement QR code generation system (Johnny)
- ✅ Build MongoDB DatabaseManager (Roger)
- ✅ Design and implement roster CSV format and upload logic (Josh, Nat)
- ✅ Test Netlify web app (not deployed yet - Sprint 3)
- 🔄 Begin UI panel integration planning
- 📋 Prepare UML diagrams for Design Review (Due W11)

## Completed Features (Sprint 2)

### 1. QR Code Generation System (Johnny) ✅
- [x] Add ZXing library dependencies to Gradle (build.gradle.kts)
- [x] Create QRCodeGenerator.java class
- [x] Implement generateSessionQRCode() method
- [x] Session ID generation (YYYYMMDD_HHMMSS format)
- [x] QR code outputs BufferedImage and PNG file
- [x] Testing and validation with sample session IDs


### 2. MongoDB Integration (Roger) ✅
- [x] Set up MongoDB Atlas cluster
- [x] Create DatabaseManager.java class
- [x] Implement session management (createSession, closeSession)
- [x] Implement attendance tracking (markAttendance, getAttendanceBySession, getAttendanceByClass)
- [x] Design MongoDB collections schema:
  - **sessions:** classId, sessionId, createdAt, weatherData, active
  - **attendance:** classId, sessionId, studentId, studentName, checkInTime, status
  - **classes:** classId, className, semester, year, startDate, endDate, professorName
  - **students:** studentId, studentName, classId
- [x] Connection string configuration in config.properties
- [x] Create dedicated MongoDB user (weatherguard_app)

### 3. Roster Management System (Josh, Nat) ✅
- [x] Design CSV format (class metadata + student list)
- [x] Create sample rosters:
  - `resources/BIO101_Roster.csv` (5 students: S001-S005)
  - `resources/CHEM201_Roster.csv` (5 students: S006-S010)
- [x] Implement uploadRosterCsv() in DatabaseManager
- [x] Implement validateStudent() method (4-check validation)
- [x] Test roster upload and validation
- [x] Auto-upload on Main.java startup

### 4. Netlify Web App Testing (Team) 🧪

- [x] Create netlify-checkin folder structure
- [x] Build student check-in HTML interface (index.html)
- [x] Implement serverless function (functions/checkin.js)
- [x] Set up MongoDB connection in Netlify function
- [x] Implement 4-level student validation:
  - ✅ Student ID exists?
  - ✅ Enrolled in correct class?
  - ✅ Name matches roster?
  - ✅ Already checked in?
- [x] Configure environment variables (local testing)
- [x] Test check-in workflow locally
- ⏳ **Production deployment planned for Sprint 3**

### 5. UI Integration Planning (Nat, Josh) 🔄
- [ ] Wire Weather.java data to Panel_WeatherInformation
- [ ] Wire StaticMap.java to Panel_WeatherMap
- [ ] Design Admin View layout for roster upload
- [ ] Plan Panel_AttendanceTable QR display integration
- [ ] Mock up historical data view in Panel_AttendanceData
- **Status:** Deferred to Sprint 3 to focus on backend completion

## Team Meetings

### Sprint 2 Planning: 10/13/2025 @ 4:00 PM
- **When:** 10/13/2025 @ 4:00 PM - 5:15 PM
- **Who:** Nat, Johnny, Roger
- **Where:** In-person (Class)
- **What:**
  - Josh leading as Sprint 2 Manager
  - Sprint 2 backlog review and task breakdown
  - Task assignments:
    - Johnny: QR code generation (ZXing integration)
    - Roger: MongoDB DatabaseManager class
    - Josh & Nat: Roster CSV design and UI planning
  - Discussed Lab9 requirements (Component-Level Design)
  - Set up Discord pairing sessions schedule

### Team Stand-up: 10/15/2025 @ 4:00 PM
- **When:** 10/15/2025 @ 4:00 PM - 5:15 PM
- **Who:** Josh, Nat, Johnny
- **Where:** In-person (Class)
- **What:**
  - Johnny: Started QRCodeGenerator.java, researching ZXing
  - Roger: Setting up MongoDB Atlas cluster, testing connections
  - Josh: Designing roster CSV format
  - Nat: Planning UI integration approach
  - Discussed integration timeline
  - Concern: Johnny struggling with ZXing dependencies (Josh offered to pair on Saturday)

### Stand-up: 10/17/2025 @ 4:00 PM
- **When:** 10/17/2025 @ 4:00 PM - 5:15 PM
- **Who:** Josh, Nat, Johnny, Roger
- **Where:** In-person (Class)
- **What:**
  - Johnny: Still working through ZXing setup issues
  - Roger: MongoDB Atlas cluster created, testing CRUD operations
  - Josh: CSV format designed, started roster parsing logic
  - Nat: Researching how to refresh UI panels dynamically
  - Team discussion: How to handle Netlify environment variables?
  - Josh suggested testing locally first, then deploy
  - Scheduled pairing session for Sunday (Josh + Johnny)

### Pairing Session 1: 10/19/2025 @ 6:00 PM
- **When:** 10/19/2025 @ 6:00 PM - 8:30 PM
- **Who:** Josh, Johnny
- **Where:** Discord (screen share session)
- **What:**
  - Josh helped Johnny troubleshoot QRCodeGenerator.java setup
  - **Breakthrough:** Fixed Gradle/Java compatibility issue (Java 25 → ms-21)
  - Added ZXing and org.json dependencies to build.gradle.kts
  - Tested QR code generation with sample session IDs
  - Johnny's QR code implementation working! 🎉
  - Random discussion: Should QR codes be saved as files or just displayed? (Both!)
  - Johnny will finish testing on his own

### Stand-up: 10/20/2025 @ 4:00 PM
- **When:** 10/20/2025 @ 4:00 PM - 5:15 PM
- **Who:** Josh, Nat, Johnny
- **Where:** In-person (Class)
- **What:**
  - Johnny: QR code generation working! Ready to demo
  - Roger: MongoDB connection working, need help with schema design
  - Josh: Roster CSV parsing complete, ready to integrate with DB
  - Nat: UI panel refresh logic designed, waiting for backend integration
  - Scheduled pairing session for Wednesday (Nat + Roger)

### Pairing Session 2: 10/22/2025 @ 7:00 PM
- **When:** 10/22/2025 @ 7:00 PM - 9:00 PM
- **Who:** Nat, Roger, Josh (joined later)
- **Where:** Discord
- **What:**
  - Nat helped Roger set up DatabaseManager.java structure
  - Created dedicated MongoDB user (weatherguard_app) with proper permissions
  - Roger implemented CRUD operations for all 4 collections
  - Tested MongoDB connection successfully with sample data
  - Josh joined at 8:00 PM to discuss UI integration approach
  - Random thoughts: Should we add a "Delete Student" function? (No, roster is source of truth)
  - Discussed Netlify deployment strategy for Friday

### Stand-up: 10/24/2025 @ 4:00 PM
- **When:** 10/24/2025 @ 4:00 PM - 5:15 PM
- **Who:** Josh, Nat, Johnny
- **Where:** In-person (Class)
- **What:**
  - Johnny: QR code system complete and tested ✅
  - Roger: DatabaseManager complete with all CRUD operations ✅
  - Josh: Roster CSV upload working, need to integrate validation ⏳
  - Nat: UI panels still not wired up (deferred to Sprint 3)
  - Concern: Netlify testing might take longer than expected

### Sprint 2 Review & Retrospective: 10/26/2025 @ 3:00 PM
- **When:** 10/26/2025 @ 3:00 PM - 5:00 PM
- **Who:** Josh, Nat, Johnny, Roger
- **Where:** Discord
- **What:**
  - **Demonstrations:**
    - Johnny demonstrated QR code generation (showed live QR code for BIO101 session)
    - Roger demonstrated MongoDB integration and all CRUD operations
    - Josh & Nat showed roster upload and 4-level validation system
    - Team tested Netlify web app locally - submitted test check-in successfully! 🎉
  - **Sprint Statistics:**
    - 3 major features implemented (QR, MongoDB, Roster)
    - 4 MongoDB collections designed and implemented
    - 2 sample rosters created (10 students total)
    - 1 Netlify web app built (not deployed yet)
  - **Retrospective discussion** (see below)
  - **Sprint 3 Planning Preview:**
    - Priority #1: Deploy Netlify to production (wguard.netlify.app)
    - Priority #2: Wire UI panels to backend data
    - Priority #3: Testing and behavioral patterns
    - Nat volunteered as Sprint 3 Manager
  - **Random discussion:**
    - Should we add attendance analytics? (Maybe Sprint 4 if we have time)
    - Roger suggested adding attendance percentage calculator (good idea!)

## Sprint 2 Retrospective

### What Went Well ✅
- All backend systems implemented successfully
- QR code generation working perfectly
- MongoDB integration robust with all collections
- Roster validation system comprehensive (4-check system)
- Netlify web app tested and working locally

### What Could Be Improved 🔄
- UI panel integration deferred to Sprint 3
- Testing could be more formalized (unit tests needed)

### Action Items for Sprint 3 🎯
- Deploy Netlify to production (wguard.netlify.app)
- Wire all UI panels to backend data
- Implement Admin View for roster management
- Add session management UI (Start/End Session buttons)
- Real-time attendance updates in UI
- Unit testing for all core classes
- Behavioral design patterns implementation

## Sprint 2 Deliverables ✅
- ✅ QRCodeGenerator.java with ZXing integration
- ✅ DatabaseManager.java with MongoDB Atlas connection
- ✅ Roster CSV format and upload functionality
- ✅ Student validation system (4-check)
- ✅ Netlify web app (tested locally, deployment in Sprint 3)
- ✅ Sample rosters (BIO101, CHEM201)
- ✅ Updated documentation (README files)

## Deferred to Sprint 3
- UI panel integration
- Admin View implementation
- Session management UI
- Real-time attendance updates
- Production Netlify deployment
