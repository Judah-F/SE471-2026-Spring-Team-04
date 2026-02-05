# WeatherGuard Attendance

**Team 04** | SE471 Spring 2026
*Judah Fisher, Josh Clemens, Chima Ohaechesi, Roger Karam*

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


## License

This project was developed as part of the SE370 Software Engineering course at California State University San Marcos.

## Contact

For questions or issues, please open an issue on GitHub or contact the development team.

---

**Version**: v1.0.0
**Last Updated**: December 2025
