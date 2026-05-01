# Command Reference - Hybrid Emergency Response System

## Quick Command Guide

### 🏗️ Build Commands

```bash
# Full clean build
./gradlew clean build

# Build without tests
./gradlew build -x test

# Build with verbose output
./gradlew build --info

# Build JAR only
./gradlew bootJar

# Build and show dependencies
./gradlew build dependencies
```

### 🚀 Run Commands

```bash
# Run with gradle
./gradlew bootRun

# Run JAR directly
java -jar build/libs/AHybridEmergencyResponseSystem-1.0-SNAPSHOT.jar

# Run on different port
java -Dserver.port=8081 -jar build/libs/AHybridEmergencyResponseSystem-1.0-SNAPSHOT.jar

# Run with debug output
./gradlew bootRun --debug
```

### 🧪 Test Commands

```bash
# Run all tests
./gradlew test

# Run specific test class
./gradlew test --tests TestClassName

# Run with coverage
./gradlew test jacocoTestReport

# Skip tests during build
./gradlew build -x test
```

### 🧹 Clean Commands

```bash
# Clean build directory
./gradlew clean

# Clean and rebuild
./gradlew clean build

# Clean everything including cache
./gradlew clean --no-daemon
```

### 📦 Dependency Commands

```bash
# Show all dependencies
./gradlew dependencies

# Show dependencies in tree format
./gradlew dependencies --configuration compile

# Update dependencies
./gradlew refresh-dependencies
```

### 🔍 Diagnostic Commands

```bash
# Check Java version
java -version

# Check Gradle version
./gradlew --version

# Verify build
./gradlew build --dry-run

# Generate build info
./gradlew assemble
```

### 🐛 Debugging Commands

```bash
# Run with debug mode on port 5005
./gradlew bootRun --debug-jvm

# Verbose logging
./gradlew build -i

# Show stack traces
./gradlew build -S

# Parallel build (faster)
./gradlew build --parallel
```

## 📱 cURL API Testing Commands

### Authentication

```bash
# Register User
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName":"John",
    "lastName":"Doe",
    "email":"john@test.com",
    "phoneNumber":"9876543210",
    "password":"Test123",
    "latitude":28.7041,
    "longitude":77.1025,
    "deviceId":"dev001"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email":"john@test.com",
    "password":"Test123"
  }'

# Get Profile
curl -X GET http://localhost:8080/api/auth/profile \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### SOS Alerts

```bash
# Trigger SOS
curl -X POST http://localhost:8080/api/sos/trigger \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "latitude":28.7041,
    "longitude":77.1025,
    "triggerType":"MANUAL_SOS",
    "isRelayedViaLoRa":false
  }'

# Get SOS History
curl -X GET http://localhost:8080/api/sos/history \
  -H "Authorization: Bearer YOUR_TOKEN"

# Get Active Alerts
curl -X GET http://localhost:8080/api/sos/active \
  -H "Authorization: Bearer YOUR_TOKEN"

# Update Alert Status
curl -X PUT "http://localhost:8080/api/sos/101/status?status=RESPONDED" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### GPS Tracking

```bash
# Track Location
curl -X POST http://localhost:8080/api/gps/track \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "latitude":28.7041,
    "longitude":77.1025,
    "accuracy":10.5,
    "speed":20.0,
    "altitude":150.0,
    "deviceId":"dev001"
  }'

# Get Tracking History
curl -X GET "http://localhost:8080/api/gps/history?hours=24" \
  -H "Authorization: Bearer YOUR_TOKEN"

# Get Latest Location
curl -X GET http://localhost:8080/api/gps/latest \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Incident Reports

```bash
# Create Report
curl -X POST http://localhost:8080/api/incident/report \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "description":"Theft attempt",
    "latitude":28.7041,
    "longitude":77.1025,
    "incidentType":"THEFT",
    "severityLevel":3
  }'

# Get My Reports
curl -X GET http://localhost:8080/api/incident/my-reports \
  -H "Authorization: Bearer YOUR_TOKEN"

# Get Pending Reports
curl -X GET http://localhost:8080/api/incident/pending \
  -H "Authorization: Bearer YOUR_TOKEN"

# Update Report Status
curl -X PUT "http://localhost:8080/api/incident/301/status?status=ACKNOWLEDGED" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Transport Verification

```bash
# Create Transport QR
curl -X POST "http://localhost:8080/api/transport/create?vehicleNumber=MH01AB1234&driverName=John&phoneNumber=9876543210&licenseNumber=DL1234567" \
  -H "Authorization: Bearer YOUR_TOKEN"

# Check QR Code
curl -X GET "http://localhost:8080/api/transport/check?qrCode=YOUR_QR_CODE" \
  -H "Authorization: Bearer YOUR_TOKEN"

# Verify Transport
curl -X POST "http://localhost:8080/api/transport/verify?qrCode=YOUR_QR_CODE&photoUrl=http://example.com/photo.jpg" \
  -H "Authorization: Bearer YOUR_TOKEN"

# Get QR Image
curl -X GET "http://localhost:8080/api/transport/qr-image?qrCode=YOUR_QR_CODE" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  > qrcode.png
```

### Risk Analysis

```bash
# Analyze Risks
curl -X POST http://localhost:8080/api/risk/analyze \
  -H "Authorization: Bearer YOUR_TOKEN"

# Get Risk Clusters
curl -X GET http://localhost:8080/api/risk/clusters \
  -H "Authorization: Bearer YOUR_TOKEN"

# Get Nearby Risks
curl -X GET "http://localhost:8080/api/risk/nearby?latitude=28.7041&longitude=77.1025" \
  -H "Authorization: Bearer YOUR_TOKEN"

# Get High-Risk Zones
curl -X GET http://localhost:8080/api/risk/high-risk \
  -H "Authorization: Bearer YOUR_TOKEN"

# Get Critical-Risk Zones
curl -X GET http://localhost:8080/api/risk/critical-risk \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## 🐳 Docker Commands

```bash
# Build Docker image
docker build -t emergency-response-system .

# Run Docker container
docker run -p 8080:8080 emergency-response-system

# Run with environment variables
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://..." \
  -e SPRING_DATASOURCE_USERNAME="user" \
  -e SPRING_DATASOURCE_PASSWORD="pass" \
  emergency-response-system

# Run in background
docker run -d -p 8080:8080 emergency-response-system

# View logs
docker logs container_id

# Stop container
docker stop container_id

# Remove container
docker rm container_id
```

## 🔧 Environment Variables

```bash
# For local development
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/neondb"
export SPRING_DATASOURCE_USERNAME="neondb_owner"
export SPRING_DATASOURCE_PASSWORD="npg_5wRfogI9nGuM"
export SERVER_PORT="8080"

# For production (use secure method to set these)
export APP_SECURITY_JWT_SECRET="your-secret-key"
export APP_SECURITY_JWT_EXPIRATION="86400000"
```

## 📊 Monitoring Commands

```bash
# Check system resources
top

# Monitor Java process
jps -l

# Monitor network
netstat -an | grep 8080

# Check disk usage
du -sh .

# Monitor logs (Unix/Linux)
tail -f logs/application.log

# Monitor logs (Windows PowerShell)
Get-Content logs/application.log -Wait
```

## 🔐 Security Commands

```bash
# Generate new JWT secret
openssl rand -base64 32

# Check SSL certificate
keytool -list -keystore keystore.jks

# Verify password hash
# Use BCrypt online tool or integrate in code

# Test API security
curl -v http://localhost:8080/api/protected-endpoint
```

## 📈 Performance Commands

```bash
# Build with parallel execution (faster)
./gradlew build --parallel --max-workers=4

# Run with JVM optimizations
java -Xmx1024m -Xms512m -jar app.jar

# Profile the application
./gradlew bootRun --args='--spring.profiles.active=production'

# Check build time
./gradlew build --profile
```

## 🎯 Quick Workflow Commands

```bash
# Complete development cycle
./gradlew clean build && ./gradlew bootRun

# Test before commit
./gradlew clean build

# Deploy to production
./gradlew bootJar && docker build -t prod:latest .

# Quick API test after startup
curl -X GET http://localhost:8080/api/auth/profile
```

## 📝 Gradle Wrapper Commands

```bash
# On Windows
gradlew build

# On Linux/Mac
./gradlew build

# Initialize gradle wrapper
gradle wrapper --gradle-version 8.10

# Update gradle wrapper
./gradlew wrapper --gradle-version 8.10
```

## 🚨 Troubleshooting Commands

```bash
# Clear gradle cache
./gradlew clean

# Clear gradle daemon
./gradlew --stop

# Rebuild everything
./gradlew cleanBuild build

# Force update dependencies
./gradlew build --refresh-dependencies

# Check compilation
./gradlew compileJava

# Detailed error report
./gradlew build --stacktrace --debug
```

## 💾 Backup & Deployment

```bash
# Create backup
tar -czf backup-$(date +%Y%m%d).tar.gz AHybridEmergencyResponseSystem/

# Create JAR backup
cp build/libs/AHybridEmergencyResponseSystem-1.0-SNAPSHOT.jar \
   backup-$(date +%Y%m%d-%H%M%S).jar

# Deploy to server
scp build/libs/AHybridEmergencyResponseSystem-1.0-SNAPSHOT.jar user@server:/app/

# Start remote application
ssh user@server "cd /app && java -jar AHybridEmergencyResponseSystem-1.0-SNAPSHOT.jar"
```

---

**Save this file for quick command reference during development and deployment!**

Version: 1.0.0  
Last Updated: April 16, 2026

