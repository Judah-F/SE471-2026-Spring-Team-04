# Sprint 1: Project Setup & Core Architecture
**Duration:** 9/29/2025 - 10/12/2025 (2 weeks)
**Sprint Manager:** Roger Karam

## Sprint Goals
- ✅ Project repository setup
- ✅ Requirements analysis (Lab_06_Requirements.pdf)
- ✅ Weather API integration (OpenWeatherMap)
- ✅ Basic UI layout (2x2 grid with JSplitPane)
- ✅ Research potential technologies (MongoDB, QR codes, Netlify)

## Completed Features
1. **Weather System** (Weather.java, Forecast.java, StaticMap.java)
   - Current weather retrieval
   - 5-day forecast
   - Weather map visualization
   - OpenWeatherMap API integration

2. **UI Framework** (Panel classes, JSplitPane layout)
   - 2x2 grid layout with resizable panels
   - Panel_WeatherInformation (placeholder)
   - Panel_WeatherMap (placeholder)
   - Panel_AttendanceTable (placeholder)
   - Panel_AttendanceData (placeholder)

3. **Technology Research** (No implementation yet - Sprint 2 work)
   - Johnny: QR code generation libraries (ZXing)
   - Roger: MongoDB database options and setup
   - Josh: Netlify serverless deployment research

## Team Meetings

**Note:** Professor was out the week of 9/29 - 10/5.

### Sprint 1 Planning: 9/29/2025 @ 4:00 PM
- **When:** 9/29/2025 @ 4:00 PM - 5:15 PM
- **Who:** Josh Clemens, Nat Grimm, Johnny Huynh, Roger Karam
- **Where:** Discord (Professor out this week)
- **What:**
  - Sprint 1 kickoff with Roger as Sprint Manager
  - Reviewed Lab_06_Requirements.pdf together
  - Discussed project scope and timeline concerns
  - Planned architecture and component breakdown
  - Task assignments:
    - Josh: Weather API integration (Weather.java, Forecast.java, StaticMap.java)
    - Nat: Java Swing UI setup (JSplitPane 2x2 grid, panel structure)
    - Roger: Research MongoDB database solutions
    - Johnny: Research QR code generation libraries
  - Concerns raised: Need to decide on database early (Sprint 2)

### Stand-up: 10/1/2025 @ 4:00 PM
- **When:** 10/1/2025 @ 4:00 PM - 5:15 PM
- **Who:** Josh, Nat, Johnny, Roger
- **Where:** Discord (Professor out this week)
- **What:**
  - Josh: Weather API integration in progress, testing OpenWeatherMap API
  - Nat: UI panels structure designed, working on JSplitPane layout
  - Roger: Researching MongoDB Atlas and setup options, comparing with local SQLite
  - Johnny: Researching ZXing library documentation for QR codes
  - Discussed API key management and config.properties approach
  - Concerns: Josh mentioned Gradle dependency issues, team helped troubleshoot

### Work Session: 10/3/2025 @ 7:00 PM
- **When:** 10/3/2025 @ 7:00 PM - 9:00 PM
- **Who:** Josh, Nat, Johnny, Roger
- **Where:** Discord
- **What:**
  - Josh demonstrated Weather.java API calls working (current weather + forecast)
  - Nat showed resizable panel layout, discussed JSplitPane resize behavior
  - Johnny shared ZXing documentation findings, showed sample QR code examples
  - Roger discussed MongoDB vs. local database pros/cons
  - Team decided on MongoDB Atlas for cloud accessibility
  - Discussed how to structure the roster (CSV vs direct DB entry)
  - Code review and feedback
  - Random discussion: Should we allow manual attendance entry? (Decided: Future feature)

### Stand-up: 10/6/2025 @ 4:00 PM
- **When:** 10/6/2025 @ 4:00 PM - 5:15 PM
- **Who:** Josh, Nat, Johnny
- **Where:** In-person (Class - Professor back!)
- **What:**
  - Josh: Weather API integration complete, working on StaticMap visualization
  - Nat: UI layout finished, panels are resizable and structured
  - Roger: Compiled MongoDB research, presented recommendation (MongoDB Atlas)
  - Johnny: QR code library research complete, ZXing is the best option
  - Discussed Lab_06 requirements and deliverables
  - Team concern: Are we on track? (Yes, but need to pick up pace on integration)

### Stand-up: 10/8/2025 @ 4:00 PM
- **When:** 10/8/2025 @ 4:00 PM - 5:15 PM
- **Who:** Josh, Nat, Johnny, Roger
- **Where:** In-person (Class)
- **What:**
  - Josh: All weather components done (Weather.java, Forecast.java, StaticMap.java)
  - Nat: Working on panel styling and layout polish
  - Roger: Started drafting MongoDB schema design for Sprint 2
  - Johnny: Thinking about how QR codes will integrate with UI
  - Brief discussion on Netlify vs AWS vs Heroku for web deployment
  - Josh suggested Netlify Functions (serverless, easy deployment)
  - Concern: How will students access check-in? QR code → URL → Web form

### Work Session: 10/10/2025 @ 8:00 PM
- **When:** 10/10/2025 @ 8:00 PM - 10:00 PM
- **Who:** Josh, Nat, Johnny
- **Where:** Discord
- **What:**
  - Final polish on Sprint 1 deliverables
  - Roger finalized MongoDB research document for Sprint Review
  - Discussed Sprint 2 timeline and task distribution
  - Planned who will be Sprint Manager for Sprint 2
  - Random thoughts: Should QR codes expire?

### Sprint 1 Review & Retrospective: 10/12/2025 @ 2:00 PM
- **When:** 10/12/2025 @ 2:00 PM - 4:00 PM
- **Who:** Josh, Nat, Johnny, Roger
- **Where:** Discord
- **What:**
  - **Demonstrations:**
    - Josh demonstrated working weather system (all 3 classes: Weather, Forecast, StaticMap)
    - Nat showed completed UI framework with resizable 2x2 panel layout
    - Roger presented MongoDB research findings (recommended MongoDB Atlas for cloud access)
    - Johnny presented QR code library research (recommended ZXing for Java compatibility)
    - Josh discussed Netlify serverless deployment research
  - **Retrospective discussion** (see below)
  - **Sprint 2 Planning:**
    - Decided to implement QR + MongoDB in Sprint 2
    - Josh volunteered as Sprint 2 Manager
    - Assigned initial tasks for Sprint 2
  - **Random concerns raised:**
    - Should we implement session timeouts? (Yes, in Sprint 2)
    - How to handle students without phones? (Future: manual entry option)
    - What if MongoDB goes down? (Add error handling in Sprint 2)

## Sprint Retrospective

### What Went Well
- Weather API integration completed successfully
- UI framework laid out with resizable panels
- Good research on technology options (MongoDB, ZXing, Netlify)
- Team collaboration and communication strong
- Lab assignments completed on time

### What Could Be Improved
- UI panels still empty (placeholder code only)
- Documentation needs to be kept up-to-date during sprint
- Could have started integration sooner

### Action Items for Next Sprint
- Implement QR code generation (Johnny)
- Implement MongoDB integration (Roger)
- Design roster CSV format and upload system (Josh, Nat)
- Begin wiring UI panels to backend data
- Start planning Netlify deployment

## Deliverables
- ✅ Working weather system (Weather.java, Forecast.java, StaticMap.java)
- ✅ UI framework (2x2 panel layout)
- ✅ Technology research report (MongoDB, QR codes, Netlify)
- ✅ Lab assignments completed
- ✅ Documentation (README files)
