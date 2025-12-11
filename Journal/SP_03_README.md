# Sprint 3: Production Deployment & Testing
**Duration:** 10/27/2025 - 11/9/2025 (2 weeks)
**Sprint Manager:** Nat Grimm

## Sprint Goals
- ✅ Deploy Netlify web app to production (wguard.netlify.app)
- ✅ End-to-end testing of full check-in workflow
- ✅ Research UI framework options (JavaFX vs Swing)
- ✅ Bug fixes and validation improvements
- ✅ Documentation and code cleanup

## Completed Features

### 1. Netlify Production Deployment (Team) ✅
- [x] Configure production environment variables in Netlify dashboard
- [x] Deploy to **wguard.netlify.app** (production)
- [x] Test end-to-end check-in workflow in production
- [x] Verify 4-level student validation working:
  - ✅ Student ID exists in system
  - ✅ Student enrolled in correct class
  - ✅ Student name matches roster
  - ✅ Duplicate check-in prevention
- [x] Monitor and fix deployment issues
- [x] Test with multiple students checking in concurrently

**Results:** Netlify app successfully deployed and tested. All validation checks working correctly in production.

### 2. End-to-End Testing (Team) ✅
- [x] Test successful check-ins for BIO101 and CHEM201
- [x] Test validation failures:
  - Unknown student ID rejection
  - Wrong class enrollment rejection
  - Name mismatch rejection
  - Duplicate check-in prevention
- [x] Verify MongoDB attendance records created correctly
- [x] Test QR code generation and scanning workflow
- [x] Load testing with multiple concurrent users

**Testing Results:**
- ✅ All validation checks passed
- ✅ MongoDB records accurate
- ✅ QR codes scannable on mobile devices
- ✅ No critical bugs found

### 3. UI Framework Research (Nat, Josh) 🔄
- [x] Research JavaFX as alternative to Swing
- [x] Evaluate pros/cons of migration
- [x] Team discussion on UI framework decision
- [x] Prototype simple JavaFX panel
- [x] Compare development time vs remaining schedule

**Decision:** Stick with Java Swing due to time constraints. JavaFX migration would delay other priorities. UI panels remain placeholder for MVP.

### 4. Bug Fixes & Improvements (Team) ✅
- [x] Fix MongoDB connection error handling
- [x] Improve Netlify function error messages
- [x] Add case-insensitive name matching
- [x] Handle network timeouts gracefully
- [x] Improve QR code session ID generation
- [x] Add validation error details to user

### 5. Documentation & Code Cleanup  ✅
- [x] Document test scenarios and results
- [x] Clean up debug print statements
- [x] Add code comments for complex logic
- [x] Update README files

## Team Meetings

### Sprint 3 Planning: 10/27/2025 @ 4:00 PM
- **When:** 10/27/2025 @ 4:00 PM - 5:15 PM
- **Who:** Josh, Nat, Johnny, Roger
- **Where:** In-person (Class)
- **What:**
  - Nat leading as Sprint 3 Manager
  - Sprint 3 backlog review and priorities
  - Task assignments:
    - Josh & Nat: Netlify production deployment
    - Roger: MongoDB monitoring and error handling
    - Johnny: QR code testing on mobile devices
  - Discussed UI framework options (Swing vs JavaFX)
  - Set goal: Get Netlify deployed by end of Week 1

### Stand-up: 10/29/2025 @ 4:00 PM
- **When:** 10/29/2025 @ 4:00 PM - 5:15 PM
- **Who:** Josh, Nat, Johnny, Roger
- **Where:** In-person (Class)
- **What:**
  - Josh: Netlify environment variables configured, deploying today
  - Nat: Researching JavaFX documentation, comparing to Swing
  - Roger: Adding error handling to DatabaseManager
  - Johnny: Testing QR codes with different phone cameras
  - Concern: Should we switch to JavaFX? (Team leaning towards "no")

### Work Session: 10/30/2025 @ 7:00 PM
- **When:** 10/30/2025 @ 7:00 PM - 9:30 PM
- **Who:** Josh, Nat, Johnny, Roger
- **Where:** Discord
- **What:**
  - **MILESTONE:** Netlify deployed to production! 🎉
  - Tested first successful check-in: Josh scanned BIO101 QR on his phone
  - Found bug: Name validation was case-sensitive (fixed by Roger)
  - Johnny tested all validation failure scenarios - all working
  - Nat demonstrated JavaFX prototype - looks good but would take weeks to migrate
  - Team decision: Stick with Swing, focus on backend quality
  - Random discussion: Should QR codes expire after X hours? (Maybe Sprint 4)

### Stand-up: 11/3/2025 @ 4:00 PM
- **When:** 11/3/2025 @ 4:00 PM - 5:15 PM
- **Who:** Josh, Nat, Johnny, Roger
- **Where:** In-person (Class)
- **What:**
  - Josh: Netlify app stable, no errors in logs
  - Nat: JavaFX research complete, recommendation is to stay with Swing
  - Roger: Error handling improved, MongoDB connection more robust
  - Johnny: All mobile QR code testing passed (iPhone, Android)
  - Discussed Sprint 4 priorities (testing, UI polish, bug fixes)

### Pairing Session: 11/5/2025 @ 6:00 PM
- **When:** 11/5/2025 @ 6:00 PM - 8:00 PM
- **Who:** Nat, Johnny, Josh
- **Where:** Discord
- **What:**
  - Nat and Johnny worked on improving Netlify error messages
  - Added detailed validation feedback (which check failed, why)
  - Johnny tested with invalid data - error messages much clearer
  - Random thoughts: Should we add a "session history" view? (Deferred to future)

### Stand-up: 11/6/2025 @ 4:00 PM
- **When:** 11/6/2025 @ 4:00 PM - 5:15 PM
- **Who:** Josh, Nat, Johnny, Roger
- **Where:** In-person (Class)
- **What:**
  - Josh: Documentation updated
  - Nat: UI framework decision finalized - Swing it is!
  - Roger: MongoDB queries optimized, response times faster
  - Johnny: QR code generation tested with 100+ sessions - no issues
  - Team feeling good about progress - ahead of schedule

### Sprint 3 Review & Retrospective: 11/9/2025 @ 3:00 PM
- **When:** 11/9/2025 @ 3:00 PM - 5:00 PM
- **Who:** Josh, Nat, Johnny, Roger
- **Where:** Discord
- **What:**
  - **Demonstrations:**
    - Josh demonstrated live production check-in (scanned QR on phone, checked in successfully)
    - Nat showed JavaFX prototype vs current Swing UI (team agreed Swing is fine for MVP)
    - Roger demonstrated improved error handling (network failures, invalid data)
    - Johnny showed mobile testing results (worked on 6 different devices)
    - Team tested all validation scenarios live - everything working!
  - **Sprint Statistics:**
    - Netlify deployed to production ✅
    - 100+ successful test check-ins
    - 15+ validation failure scenarios tested
    - Zero critical bugs found
    - UI framework decision: Stay with Swing
  - **Retrospective discussion** (see below)
  - **Sprint 4 Planning Preview:**
    - Priority #1: Comprehensive testing (unit tests, integration tests)
    - Priority #2: Bug fixes and polish
    - Priority #3: UI improvements (if time permits)
    - Johnny volunteered as Sprint 4 Manager
  - **Random discussion:**
    - Should we add admin UI for roster management? (Nice-to-have for Sprint 4)
    - Roger suggested attendance analytics (percentage, trends) - interesting idea!

## Sprint 3 Retrospective

### What Went Well ✅
- Netlify deployment smooth and successful
- All validation checks working perfectly in production
- Good team decision on UI framework (pragmatic choice)
- No major blockers or issues
- Great team collaboration and communication
- Ahead of schedule on core features

### What Could Be Improved 🔄
- UI panels still placeholder (deferred to Sprint 4)
- Could have started JavaFX research earlier (to make informed decision sooner)
- Testing could be more automated (manual testing is time-consuming)
- No unit tests yet (deferred to Sprint 4)

### Lessons Learned 💡
- MVP approach works - focus on core features first
- Production testing reveals issues that local testing misses
- Clear error messages make debugging much easier
- Mobile testing is critical for QR code workflow

### Action Items for Sprint 4 🎯
- Write comprehensive JUnit tests for all core classes
- Add integration tests for end-to-end workflows
- Improve UI panels (at least wire to backend data)
- Add admin UI for roster management (stretch goal)
- Performance testing and optimization
- Bug fixes and polish

## Sprint 3 Deliverables ✅
- ✅ Netlify web app deployed to production (wguard.netlify.app)
- ✅ All validation scenarios tested and working
- ✅ Bug fixes and error handling improvements
- ✅ UI framework research and decision (Swing)
- ✅ Clean, stable codebase ready for Sprint 4

## Deferred to Sprint 4
- Unit testing (JUnit)
- Integration testing
- UI panel integration with backend
- Admin UI for roster management
- Attendance analytics and reporting
- Performance optimization