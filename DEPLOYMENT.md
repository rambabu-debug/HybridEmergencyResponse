# Deployment Guide - Hybrid Emergency Response System

## Quick Start

### Prerequisites
- Java 17+
- PostgreSQL (Neon Cloud)
- Git
- Gradle 8.10+

### Step 1: Clone & Build

```bash
cd C:\Users\ramba\Downloads\demo\AHybridEmergencyResponseSystem
./gradlew clean build
```

### Step 2: Run the Application

```bash
./gradlew bootRun
```

The application will be available at: `http://localhost:8080`

### Step 3: Access the Application

1. Open your browser
2. Navigate to `http://localhost:8080`
3. Register a new account or login
4. Grant location permissions when prompted

## Database Setup

The application uses PostgreSQL with Neon Cloud. The database connection is already configured in `application.yml`:

```
URL: postgresql://neondb_owner:npg_5wRfogI9nGuM@ep-cool-lab-anf63x9w-pooler.c-6.us-east-1.aws.neon.tech/neondb
```

On first run, Hibernate will automatically create all required tables:
- users
- sos_alerts
- gps_tracking
- incident_reports
- transport_verification
- risk_clusters

## Features Available

### 1. Authentication
- User registration with email and phone
- Login with JWT tokens
- Password encryption (BCrypt)
- Auto-generated device ID

### 2. SOS Alerts
- One-tap emergency alerts
- Location sharing via GPS
- Trigger type: Manual, Shake Detection, Button Press
- Encrypted message support
- LoRa relay capability flag
- Real-time alert tracking

### 3. GPS Tracking
- Real-time location tracking
- Location history (24-hour retention)
- Risk zone detection
- Speed and altitude monitoring
- Device identification

### 4. Incident Reporting
- Multiple incident types (Theft, Assault, Harassment, Accident, Suspicious Activity)
- Severity levels (1-4)
- Detailed descriptions
- Photo attachment support (URL-based)
- Geographic clustering

### 5. Transport Verification
- QR code generation for vehicles
- Vehicle information storage (Number, Driver, License)
- Verification scoring
- Photo verification

### 6. Risk Analysis
- K-Means clustering algorithm
- Automatic risk level classification
- Nearby risk zone queries
- Critical risk identification

## API Testing

### Using cURL

#### Register User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "phoneNumber": "9876543210",
    "password": "SecurePass123",
    "latitude": 28.7041,
    "longitude": 77.1025,
    "deviceId": "device_001"
  }'
```

#### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "SecurePass123"
  }'
```

#### Trigger SOS Alert
```bash
curl -X POST http://localhost:8080/api/sos/trigger \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "latitude": 28.7041,
    "longitude": 77.1025,
    "triggerType": "MANUAL_SOS",
    "encryptedMessage": "Emergency at XYZ location",
    "isRelayedViaLoRa": false
  }'
```

#### Track Location
```bash
curl -X POST http://localhost:8080/api/gps/track \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "latitude": 28.7041,
    "longitude": 77.1025,
    "accuracy": 10.5,
    "speed": 25.0,
    "altitude": 150.0,
    "deviceId": "device_001"
  }'
```

#### Get Active Alerts
```bash
curl -X GET http://localhost:8080/api/sos/active \
  -H "Authorization: Bearer YOUR_TOKEN"
```

#### Report Incident
```bash
curl -X POST http://localhost:8080/api/incident/report \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Theft attempt at parking lot",
    "latitude": 28.7041,
    "longitude": 77.1025,
    "incidentType": "THEFT",
    "severityLevel": 3
  }'
```

## Web UI Features

### Dashboard
- Real-time active SOS alerts count
- Nearby risk zones count
- Current location display
- Recent incidents count
- Live alert feed

### SOS Alert Section
- Emergency button (pulsing red)
- Manual alert trigger
- Additional details form
- SOS history

### GPS Tracking
- Real-time location display
- Start/Stop tracking
- Tracking history viewer
- Location accuracy info

### Incident Reporting
- Incident type selection
- Severity level rating
- Description form
- Photo upload support
- Report history

### Transport Verification
- QR code scanning/entry
- Transport verification
- QR code generation
- Vehicle information storage

### Risk Analysis
- Risk cluster visualization
- Risk level indicators
- Nearby risk zones
- Critical risk zones

## Configuration

### JWT Settings
Located in `application.yml`:
```yaml
app:
  security:
    jwt:
      secret: d29vZHdvcmQtc2VjdXJpdHktand0LXNlY3JldC1mb3ItaHlicmlkLWVtZXJnZW5jeS1yZXNwb25zZS1zeXN0ZW0=
      expiration: 86400000  # 24 hours
      refresh-expiration: 604800000  # 7 days
```

### Database
```yaml
spring:
  datasource:
    url: jdbc:postgresql://ep-cool-lab-anf63x9w-pooler.c-6.us-east-1.aws.neon.tech:5432/neondb?sslmode=require
    username: neondb_owner
    password: npg_5wRfogI9nGuM
  jpa:
    hibernate:
      ddl-auto: update  # Auto-create tables
```

### Server
```yaml
server:
  port: 8080
  servlet:
    context-path: /api
```

## Troubleshooting

### Issue: Port 8080 already in use
**Solution**: Change port in `application.yml`
```yaml
server:
  port: 8081
```

### Issue: Database connection failed
**Solution**: 
1. Check internet connectivity
2. Verify PostgreSQL URL is correct
3. Ensure SSL mode is enabled

### Issue: Geolocation not working
**Solution**:
1. Enable location permission in browser
2. Use HTTPS (or localhost)
3. Check browser console for errors

### Issue: Token authentication failed
**Solution**:
1. Verify token format: `Bearer <token>`
2. Check token expiration
3. Re-login to get new token

## Production Deployment

### Using Docker

Create `Dockerfile`:
```dockerfile
FROM openjdk:17-slim
COPY build/libs/AHybridEmergencyResponseSystem-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:
```bash
docker build -t emergency-response-system .
docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=your_db_url emergency-response-system
```

### Using AWS/GCP/Azure

1. Build the JAR: `./gradlew build`
2. Upload `build/libs/AHybridEmergencyResponseSystem-1.0-SNAPSHOT.jar` to cloud
3. Configure database connection via environment variables
4. Set up security groups/firewall rules
5. Enable HTTPS with SSL certificate

## Performance Optimization

1. **Database Indexing**: Indices created on frequently queried columns
2. **Connection Pooling**: Enabled by default
3. **Caching**: Implement Redis for session management
4. **API Rate Limiting**: Add rate limiters for public endpoints
5. **Static Asset Caching**: Configure CDN for CSS/JS files

## Security Checklist

- [x] JWT token-based authentication
- [x] Password encryption (BCrypt)
- [x] CORS enabled
- [x] SQL injection prevention (Hibernate)
- [x] HTTPS recommended for production
- [x] Sensitive data in application.yml
- [ ] Add rate limiting
- [ ] Add request validation
- [ ] Add API key authentication
- [ ] Add audit logging

## Monitoring

### Logs Location
- Application logs: Console output or configure in `application.yml`
- Error tracking: Check browser console
- Database logs: Check PostgreSQL logs

### Health Check
```bash
curl http://localhost:8080/health
```

## Support & Documentation

For detailed API documentation, see API Endpoints section in README.md

For feature details, see Features section in README.md

