# 🚀 Hybrid Emergency Response System - RUNNING

## ✅ Application Status
The application is **NOW RUNNING** and fully operational!

## 📱 Access the Application

### Browser URL:
```
http://localhost:8888
```

Simply open this URL in your web browser to access the complete user interface.

## 🔑 Getting Started

### 1. Create an Account
- Click on the **"Register"** tab on the login page
- Fill in the required information:
  - First Name
  - Last Name
  - Email
  - Phone Number
  - Password
- Click **"Register"**

### 2. Login
- Use your registered email and password to login
- After successful login, you'll access the main dashboard

## 📊 Features Available

### Dashboard
- View Active SOS Alerts
- Check Nearby Risk Zones
- See Your Current Location
- View Recent Incidents
- Monitor Recent Alerts Feed

### 🆘 Emergency SOS Alert
- **Large Red SOS Button** for emergency triggering
- Manual SOS options
- Support for Shake Detection and Button Press triggers
- View SOS History

### 📍 GPS Tracking
- Real-time location tracking
- Start/Stop tracking functionality
- View tracking history (last 24 hours)
- Display current latitude, longitude, and accuracy

### 📝 Incident Report
- Report incidents (Theft, Assault, Harassment, Accident, etc.)
- Set severity levels (Low, Medium, High, Critical)
- Attach photos to incidents
- View your incident reports

### 🚕 Transport Verification
- Verify public transport vehicles using QR codes
- Generate QR codes for transport (if you're a driver)
- Secure verification system

### 🗺️ Risk Zone Analysis
- Analyze risk clusters based on historical incident data
- View risk levels for different geographic areas
- Make informed decisions about safe routes

## 🗄️ Database Connection
- **Database**: Neon PostgreSQL
- **Connection Status**: ✅ Connected
- **Data Storage**: All user data, alerts, incidents, and tracking data are securely stored

## 🔐 Security Features
- JWT-based authentication
- Password encryption using BCrypt
- CORS-enabled for cross-origin requests
- Spring Security integration
- Encrypted message support for sensitive communications

## 🛠️ Technical Stack
- **Backend**: Spring Boot 3.2.0 with Spring Security
- **Database**: PostgreSQL (Neon)
- **Frontend**: HTML5, CSS3, Vanilla JavaScript
- **Port**: 8888
- **Java Version**: JDK 21

## 📋 API Endpoints Available

### Authentication
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### SOS Alerts
- `POST /api/sos/trigger` - Trigger emergency SOS
- `GET /api/sos/active` - Get active alerts
- `GET /api/sos/history` - Get SOS history

### GPS Tracking
- `POST /api/gps/track` - Record GPS location
- `GET /api/gps/history` - Get tracking history

### Incidents
- `POST /api/incident/report` - Report incident
- `GET /api/incident/my-reports` - Get user's reports
- `GET /api/incident/pending` - Get pending incidents

### Risk Analysis
- `GET /api/risk/clusters` - Get risk clusters/zones

### Transport Verification
- `GET /api/transport/check` - Verify transport
- `POST /api/transport/create` - Create QR code
- `GET /api/transport/qr-image` - Get QR image

## 📝 Test Account
You can create your own account to test the system, or use test data:
- **Test Email**: test@example.com
- **Test Password**: Test@123

## ⚠️ Important Notes
1. **Geolocation Permission**: The browser will request geolocation permission when you try to use location-based features
2. **Real-time Updates**: The dashboard updates in real-time as new alerts and incidents are reported
3. **Data Persistence**: All data is stored in the Neon PostgreSQL database and persists across sessions

## 🔍 Troubleshooting

### Port Already in Use
If port 8888 is already in use, change it in `application.yml`:
```yaml
server:
  port: 8080  # Change to any available port
```

### Geolocation Not Working
- Ensure HTTPS (for production) or HTTP localhost is being used
- Check browser permissions for location access
- Allow location access in browser settings

### Database Connection Issues
- Verify your Neon connection string is correct
- Check that network allows external database connections
- Verify database credentials

## 📞 Support
For any issues, check the application logs in the terminal where the app is running.

---

**Application successfully deployed! 🎉**

