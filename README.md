# Hybrid Emergency Response System

A comprehensive IoT-based emergency response system combining real-time tracking, LoRa mesh networking capabilities, and predictive analytics for urban safety.

## Features

### 1. **SOS Alert System**
- One-tap emergency alert with location sharing
- Shake detection for hands-free activation
- Physical button integration support
- Encrypted message transmission
- LoRa mesh relay support for offline scenarios
- Real-time alert status tracking

### 2. **GPS Real-time Tracking**
- Continuous location tracking with accuracy monitoring
- Historical tracking data with 24-hour retention
- Risk zone detection
- Speed and altitude monitoring
- Device identification for multi-device support

### 3. **Incident Reporting**
- Detailed incident categorization (Theft, Assault, Harassment, Accident, etc.)
- Severity level classification (Low, Medium, High, Critical)
- Photo attachment support
- Geographic incident clustering
- Report status tracking

### 4. **Risk Zone Analysis**
- K-Means clustering algorithm for incident hotspot identification
- Gaussian Mixture Models support
- Automatic risk level classification
- Nearby risk zone alerts
- Risk cluster analytics

### 5. **Public Transport Verification**
- QR code generation for vehicles
- Driver and vehicle verification
- Secure transport identity confirmation
- Verification scoring system
- Photo verification support

### 6. **Authentication & Security**
- JWT token-based authentication
- BCrypt password encryption
- Role-based access control
- CORS enabled for cross-origin requests
- Session management

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **ORM**: Spring Data JPA (Hibernate)
- **Security**: Spring Security with JWT
- **Database**: PostgreSQL (Neon Cloud)
- **ML**: Apache Commons Math (K-Means Clustering)
- **QR Code**: ZXing Library

### Frontend
- **HTML5** - Semantic markup
- **CSS3** - Responsive design with animations
- **JavaScript (ES6)** - Dynamic interactions
- **Geolocation API** - GPS tracking
- **Fetch API** - REST communication

### Database
- PostgreSQL with Neon Cloud
- Connection: `postgresql://neondb_owner:npg_5wRfogI9nGuM@ep-cool-lab-anf63x9w-pooler.c-6.us-east-1.aws.neon.tech/neondb`

## Project Structure

```
AHybridEmergencyResponseSystem/
├── src/
│   ├── main/
│   │   ├── java/todo/tutorials/
│   │   │   ├── Main.java (Spring Boot Application)
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── SOSAlertController.java
│   │   │   │   ├── GPSTrackingController.java
│   │   │   │   ├── IncidentReportController.java
│   │   │   │   ├── TransportVerificationController.java
│   │   │   │   ├── RiskAnalysisController.java
│   │   │   │   └── HomeController.java
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── SOSAlertService.java
│   │   │   │   ├── GPSTrackingService.java
│   │   │   │   ├── IncidentReportService.java
│   │   │   │   ├── TransportVerificationService.java
│   │   │   │   ├── RiskAnalysisService.java
│   │   │   │   └── ClusteringService.java
│   │   │   ├── entity/
│   │   │   │   ├── User.java
│   │   │   │   ├── SOSAlert.java
│   │   │   │   ├── GPSTracking.java
│   │   │   │   ├── IncidentReport.java
│   │   │   │   ├── TransportVerification.java
│   │   │   │   └── RiskCluster.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── SOSAlertRepository.java
│   │   │   │   ├── GPSTrackingRepository.java
│   │   │   │   ├── IncidentReportRepository.java
│   │   │   │   ├── TransportVerificationRepository.java
│   │   │   │   └── RiskClusterRepository.java
│   │   │   ├── dto/
│   │   │   │   ├── AuthRequest.java
│   │   │   │   ├── AuthResponse.java
│   │   │   │   ├── UserRegisterRequest.java
│   │   │   │   ├── SOSAlertRequest.java
│   │   │   │   ├── GPSTrackingRequest.java
│   │   │   │   ├── IncidentReportRequest.java
│   │   │   ├── security/
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   └── EncryptionUtil.java
│   │   │   └── config/
│   │   │       └── WebConfig.java
│   │   ├── resources/
│   │   │   ├── application.yml
│   │   │   ├── templates/
│   │   │   │   └── index.html
│   │   │   └── static/
│   │   │       ├── styles.css
│   │   │       └── app.js
│   └── test/
├── build.gradle
├── settings.gradle
└── README.md
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `GET /api/auth/profile` - Get user profile

### SOS Alerts
- `POST /api/sos/trigger` - Trigger SOS alert
- `GET /api/sos/history` - Get user's SOS history
- `GET /api/sos/active` - Get active alerts
- `GET /api/sos/area` - Get alerts in area
- `PUT /api/sos/{id}/status` - Update alert status

### GPS Tracking
- `POST /api/gps/track` - Track location
- `GET /api/gps/history` - Get tracking history
- `GET /api/gps/latest` - Get latest location

### Incident Reports
- `POST /api/incident/report` - Submit incident report
- `GET /api/incident/my-reports` - Get user's reports
- `GET /api/incident/pending` - Get pending reports
- `GET /api/incident/area` - Get reports in area
- `PUT /api/incident/{id}/status` - Update report status

### Transport Verification
- `POST /api/transport/create` - Create transport QR code
- `POST /api/transport/verify` - Verify transport
- `GET /api/transport/check` - Check transport by QR code
- `GET /api/transport/qr-image` - Get QR code image

### Risk Analysis
- `POST /api/risk/analyze` - Analyze risks
- `GET /api/risk/clusters` - Get active risk clusters
- `GET /api/risk/nearby` - Get nearby risk zones
- `GET /api/risk/high-risk` - Get high-risk clusters
- `GET /api/risk/critical-risk` - Get critical-risk clusters

## Setup Instructions

### Prerequisites
- Java 17 or higher
- PostgreSQL database access
- Git

### Installation & Running

1. **Clone the repository**
```bash
git clone <repository-url>
cd AHybridEmergencyResponseSystem
```

2. **Build the project**
```bash
./gradlew build
```

3. **Run the application**
```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## Configuration

### Database Configuration
Edit `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://neondb_owner:npg_5wRfogI9nGuM@ep-cool-lab-anf63x9w-pooler.c-6.us-east-1.aws.neon.tech:5432/neondb?sslmode=require
    username: neondb_owner
    password: npg_5wRfogI9nGuM
```

### JWT Configuration
```yaml
app:
  security:
    jwt:
      secret: <your-secret-key>
      expiration: 86400000 # 24 hours
      refresh-expiration: 604800000 # 7 days
```

## Usage

### User Registration
1. Navigate to the application home page
2. Click "Register" tab
3. Fill in user details
4. Grant location permission when prompted
5. Submit registration

### Triggering SOS Alert
1. Navigate to "SOS Alert" section
2. Click the red "TAP TO TRIGGER SOS" button
3. Confirm your emergency
4. Your location will be automatically shared

### Reporting Incident
1. Go to "Report an Incident" section
2. Select incident type and severity
3. Add description and optional photo
4. Submit report

### Tracking Location
1. Navigate to "Tracking" section
2. Click "Start Tracking" to begin continuous tracking
3. View tracking history with timestamps

### Verifying Transport
1. Go to "Transport Verification" section
2. For passengers: Scan/enter QR code to verify vehicle
3. For drivers: Generate QR code with vehicle details

### Analyzing Risk Zones
1. Navigate to "Risk Zones" section
2. Click "Analyze Risks" to run clustering algorithm
3. View identified risk clusters and their levels

## Machine Learning Features

### K-Means Clustering
The system uses K-Means++ algorithm to identify incident hotspots:
- Analyzes historical incident locations
- Automatically determines optimal number of clusters
- Classifies risk levels based on incident density

### Risk Level Classification
- **LOW**: 1-5 incidents
- **MEDIUM**: 6-10 incidents
- **HIGH**: 11-20 incidents
- **CRITICAL**: 20+ incidents

## Security Features

1. **JWT Authentication**: Token-based stateless authentication
2. **Password Encryption**: BCrypt with salt
3. **CORS Protection**: Cross-origin request validation
4. **Data Encryption**: AES encryption for sensitive messages
5. **SQL Injection Protection**: Parameterized queries via Hibernate
6. **Session Management**: No server-side session storage

## Performance Considerations

- GPS tracking updates every 5 seconds
- Alert retrieval limited to 60 minutes of recent data
- Incident clustering runs on-demand
- Database connection pooling enabled
- API responses cached where appropriate

## Troubleshooting

### Geolocation Not Working
- Check browser permission for location access
- Ensure HTTPS is used (or localhost)
- Verify browser geolocation API support

### Database Connection Issues
- Verify Neon database URL and credentials
- Check internet connectivity
- Ensure SSL mode is enabled

### JWT Token Errors
- Verify token format: `Bearer <token>`
- Check token expiration time
- Regenerate token if expired

## Contributing

1. Create feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add your feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Submit pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues and questions, please contact the development team or open an issue in the repository.

## Future Enhancements

- [ ] LoRa mesh networking integration
- [ ] Mobile app (React Native)
- [ ] Advanced mapping with Mapbox
- [ ] Real-time notifications via WebSocket
- [ ] Machine learning model improvements
- [ ] Multi-language support
- [ ] Offline data synchronization
- [ ] Advanced analytics dashboard

