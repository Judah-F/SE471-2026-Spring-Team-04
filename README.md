# WeatherGuard Attendance

**Team 02** | SE370 Fall 2025
*Nat Grimm, Josh Clemens, Johnny Huynh, Roger Karam*

## Overview

WeatherGuard Attendance is a desktop application that modernizes classroom attendance tracking through QR code-based check-ins while integrating real-time weather data. Designed for educational institutions in extreme weather regions, it helps schools correlate attendance patterns with weather conditions to support informed decision-making about operations and student safety.

## Key Features

- **QR Code Check-In**: Students scan codes with mobile devices for instant attendance recording (80% faster than manual roll call)
- **Real-Time Dashboard**: Live attendance tracking with color-coded student status indicators
- **Weather Integration**: Current conditions, 5-day forecasts, and weather maps via OpenWeatherMap API
- **Roster Management**: CSV import for quick class setup and updates
- **Data Persistence**: MongoDB database stores attendance history and correlates with weather patterns
- **Multiple Views**: Admin interface for class management, Teacher dashboard for sessions, Student web portal for check-ins

## Technology Stack

### Desktop Application
- **JavaFX**: Desktop GUI framework with FXML
- **Java 11+**: Core application language
- **Maven/Gradle**: Build and dependency management
- **ZXing**: QR code generation library

### Backend & Data
- **MongoDB Atlas**: Cloud-hosted NoSQL database
- **MongoDB Java Driver**: Database connectivity
- **Netlify**: Web portal hosting for student check-in interface

### External APIs
- **OpenWeatherMap API**: Real-time weather data and forecasts

## Quick Start

### Prerequisites
- Java Runtime Environment (JRE) 11 or higher
- MongoDB Atlas account (free tier)
- OpenWeatherMap API key (free tier)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/SE370-2025-fall-team-02.git
   cd SE370-2025-fall-team-02
   ```

2. **Configure database and API keys**
   - Create `config.properties` from `config.properties.template`
   - Add your MongoDB connection string
   - Add your OpenWeatherMap API key

3. **Build and run**
   ```bash
   # Using Gradle
   cd Code/WeatherGuard
   ./gradlew build
   ./gradlew run
   ```

### First Time Use

1. Launch the application (Admin View opens)
2. Click "Upload Roster" and select a CSV file
3. Double-click a class to open Teacher View
4. Click "Start Session" to generate QR code
5. Students scan and check in via mobile browser

## Project Structure

```
SE370-2025-fall-team-02/
├── Code/
│   ├── WeatherGuard/          # JavaFX desktop application
│   └── netlify-checkin/        # Student web portal
├── Journal/                    # Sprint documentation
├── Notes/                      # Development notes
└── README.md
```

## Documentation

- **Software Design Spec**: Detailed architecture, use cases, and design decisions (available in Downloads)
- **Sprint Reports**: See `Journal/` folder for sprint planning and retrospectives
- **User Manual**: See Software Design Spec Appendix 7.2

## Key Design Decisions

- **Layered Architecture**: Clear separation between Presentation, Business Logic, Data Access, and External Integration layers
- **Singleton Pattern**: DatabaseManager ensures single connection instance
- **Facade Pattern**: WeatherService simplifies complex weather subsystem
- **Real-Time Polling**: 2-second updates during active sessions for live dashboard

## Known Limitations

- Maximum 34 students per class (UI grid layout)
- Requires internet connectivity for all operations
- Single active session per class at a time
- No offline mode or data export functionality

## Future Enhancements

- Data export to Excel/CSV
- Email notifications for absent students
- Historical attendance analytics
- Multi-location support for hybrid classes
- Mobile app for teachers

## License

This project was developed as part of the SE370 Software Engineering course at California State University San Marcos.

## Contact

For questions or issues, please open an issue on GitHub or contact the development team.

---

**Version**: v1.0.0
**Last Updated**: December 2025
