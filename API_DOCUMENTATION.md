# API Documentation - Hybrid Emergency Response System

## Base URL
```
http://localhost:8080/api
```

## Authentication
All protected endpoints require JWT token in Authorization header:
```
Authorization: Bearer <your_jwt_token>
```

---

## 1. Authentication Endpoints

### Register User
**POST** `/auth/register`

Create a new user account.

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "phoneNumber": "+919876543210",
  "password": "SecurePass123",
  "latitude": 28.7041,
  "longitude": 77.1025,
  "deviceId": "device_uuid_001"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
  "userId": 1,
  "email": "john@example.com",
  "message": "Registration successful"
}
```

**Error Response (400):**
```json
{
  "message": "Email already exists"
}
```

---

### User Login
**POST** `/auth/login`

Authenticate user and get JWT token.

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "SecurePass123"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
  "userId": 1,
  "email": "john@example.com",
  "message": "Login successful"
}
```

**Error Response (400):**
```json
{
  "message": "Invalid credentials"
}
```

---

### Get User Profile
**GET** `/auth/profile`

Retrieve authenticated user's profile.

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
{
  "id": 1,
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+919876543210",
  "latitude": 28.7041,
  "longitude": 77.1025,
  "isActive": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

## 2. SOS Alert Endpoints

### Trigger SOS Alert
**POST** `/sos/trigger`

Send an emergency SOS alert with current location.

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "latitude": 28.7041,
  "longitude": 77.1025,
  "triggerType": "MANUAL_SOS",
  "encryptedMessage": "Emergency at parking lot near Central Park",
  "isRelayedViaLoRa": false
}
```

**Trigger Types:**
- `MANUAL_SOS` - Manual trigger
- `SHAKE_DETECTION` - Automatic shake detection
- `BUTTON_PRESS` - Physical button press
- `AUTOMATIC_ALERT` - System-generated alert

**Response (200 OK):**
```json
{
  "id": 101,
  "userId": 1,
  "latitude": 28.7041,
  "longitude": 77.1025,
  "triggerType": "MANUAL_SOS",
  "status": "ACTIVE",
  "createdAt": "2024-01-15T11:45:00",
  "encryptedMessage": "Emergency at parking lot near Central Park",
  "isRelayedViaLoRa": false
}
```

---

### Get SOS History
**GET** `/sos/history`

Retrieve user's SOS alert history.

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
[
  {
    "id": 101,
    "userId": 1,
    "latitude": 28.7041,
    "longitude": 77.1025,
    "triggerType": "MANUAL_SOS",
    "status": "RESOLVED",
    "createdAt": "2024-01-15T11:45:00",
    "respondedAt": "2024-01-15T11:50:00"
  }
]
```

---

### Get Active Alerts
**GET** `/sos/active`

Retrieve all active SOS alerts from last 60 minutes.

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
[
  {
    "id": 105,
    "userId": 5,
    "latitude": 28.6139,
    "longitude": 77.2090,
    "triggerType": "SHAKE_DETECTION",
    "status": "ACTIVE",
    "createdAt": "2024-01-15T12:30:00"
  }
]
```

---

### Get Alerts in Area
**GET** `/sos/area?minLat=28.6&maxLat=28.8&minLon=77.0&maxLon=77.2`

Retrieve alerts in specified geographic area.

**Query Parameters:**
- `minLat`: Minimum latitude
- `maxLat`: Maximum latitude
- `minLon`: Minimum longitude
- `maxLon`: Maximum longitude

**Response (200 OK):**
```json
[
  {
    "id": 105,
    "latitude": 28.7041,
    "longitude": 77.1025,
    "triggerType": "MANUAL_SOS",
    "status": "ACTIVE",
    "createdAt": "2024-01-15T12:30:00"
  }
]
```

---

### Update Alert Status
**PUT** `/sos/{id}/status?status=RESPONDED`

Update status of an SOS alert.

**Path Parameters:**
- `id`: Alert ID

**Query Parameters:**
- `status`: New status (ACTIVE, ACKNOWLEDGED, RESPONDED, RESOLVED, CANCELLED)

**Response (200 OK):**
```json
{
  "id": 101,
  "status": "RESPONDED",
  "respondedAt": "2024-01-15T11:50:00"
}
```

---

## 3. GPS Tracking Endpoints

### Track Location
**POST** `/gps/track`

Submit current GPS location for tracking.

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "latitude": 28.7041,
  "longitude": 77.1025,
  "accuracy": 10.5,
  "speed": 25.0,
  "altitude": 150.5,
  "deviceId": "device_uuid_001"
}
```

**Response (200 OK):**
```json
{
  "id": 201,
  "userId": 1,
  "latitude": 28.7041,
  "longitude": 77.1025,
  "accuracy": 10.5,
  "speed": 25.0,
  "altitude": 150.5,
  "timestamp": "2024-01-15T12:45:30",
  "isInRiskZone": false,
  "nearbyAlerts": 0
}
```

---

### Get Tracking History
**GET** `/gps/history?hours=24`

Retrieve location tracking history.

**Query Parameters:**
- `hours`: Number of hours to retrieve (default: 24)

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
[
  {
    "id": 201,
    "latitude": 28.7041,
    "longitude": 77.1025,
    "accuracy": 10.5,
    "timestamp": "2024-01-15T12:45:30"
  },
  {
    "id": 200,
    "latitude": 28.6950,
    "longitude": 77.0980,
    "accuracy": 8.2,
    "timestamp": "2024-01-15T12:40:00"
  }
]
```

---

### Get Latest Location
**GET** `/gps/latest`

Get user's most recent GPS location.

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
{
  "id": 201,
  "latitude": 28.7041,
  "longitude": 77.1025,
  "accuracy": 10.5,
  "speed": 25.0,
  "altitude": 150.5,
  "timestamp": "2024-01-15T12:45:30"
}
```

---

## 4. Incident Report Endpoints

### Create Incident Report
**POST** `/incident/report`

Submit a new incident report.

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "description": "Attempted theft in parking lot at 2 PM",
  "latitude": 28.7041,
  "longitude": 77.1025,
  "incidentType": "THEFT",
  "severityLevel": 3,
  "photoUrl": "https://example.com/photo.jpg"
}
```

**Incident Types:**
- `THEFT`
- `ASSAULT`
- `HARASSMENT`
- `ACCIDENT`
- `SUSPICIOUS_ACTIVITY`
- `OTHER`

**Severity Levels:**
- `1`: Low
- `2`: Medium
- `3`: High
- `4`: Critical

**Response (200 OK):**
```json
{
  "id": 301,
  "userId": 1,
  "description": "Attempted theft in parking lot at 2 PM",
  "latitude": 28.7041,
  "longitude": 77.1025,
  "incidentType": "THEFT",
  "status": "PENDING",
  "severityLevel": 3,
  "createdAt": "2024-01-15T13:00:00"
}
```

---

### Get My Reports
**GET** `/incident/my-reports`

Retrieve user's incident reports.

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
[
  {
    "id": 301,
    "description": "Attempted theft",
    "incidentType": "THEFT",
    "status": "PENDING",
    "severityLevel": 3,
    "createdAt": "2024-01-15T13:00:00"
  }
]
```

---

### Get Pending Reports
**GET** `/incident/pending`

Retrieve all pending incident reports (Admin).

**Response (200 OK):**
```json
[
  {
    "id": 301,
    "userId": 1,
    "description": "Attempted theft",
    "latitude": 28.7041,
    "longitude": 77.1025,
    "incidentType": "THEFT",
    "status": "PENDING",
    "severityLevel": 3
  }
]
```

---

### Get Reports in Area
**GET** `/incident/area?minLat=28.6&maxLat=28.8&minLon=77.0&maxLon=77.2&hours=24`

Retrieve incident reports in specified area.

**Query Parameters:**
- `minLat`, `maxLat`, `minLon`, `maxLon`: Geographic bounds
- `hours`: Time range (default: 24)

**Response (200 OK):**
```json
[
  {
    "id": 301,
    "description": "Attempted theft",
    "incidentType": "THEFT",
    "severityLevel": 3,
    "createdAt": "2024-01-15T13:00:00"
  }
]
```

---

### Update Report Status
**PUT** `/incident/{id}/status?status=ACKNOWLEDGED`

Update incident report status.

**Path Parameters:**
- `id`: Report ID

**Query Parameters:**
- `status`: New status (PENDING, ACKNOWLEDGED, INVESTIGATING, RESOLVED, DISMISSED)

**Response (200 OK):**
```json
{
  "id": 301,
  "status": "ACKNOWLEDGED",
  "updatedAt": "2024-01-15T13:15:00"
}
```

---

## 5. Transport Verification Endpoints

### Create Transport QR Code
**POST** `/transport/create?vehicleNumber=MH01AB1234&driverName=John&phoneNumber=9876543210&licenseNumber=DL0110989876`

Generate QR code for public transport.

**Query Parameters:**
- `vehicleNumber`: Vehicle registration number
- `driverName`: Driver's name
- `phoneNumber`: Driver's phone
- `licenseNumber`: Driver's license number

**Response (200 OK):**
```json
{
  "id": 401,
  "qrCode": "550e8400-e29b-41d4-a716-446655440000",
  "vehicleNumber": "MH01AB1234",
  "driverName": "John",
  "phoneNumber": "9876543210",
  "licenseNumber": "DL0110989876",
  "status": "PENDING",
  "isVerified": false,
  "createdAt": "2024-01-15T14:00:00"
}
```

---

### Verify Transport
**POST** `/transport/verify?qrCode=550e8400-e29b-41d4-a716-446655440000&photoUrl=https://example.com/photo.jpg`

Verify a transport vehicle.

**Query Parameters:**
- `qrCode`: QR code from vehicle
- `photoUrl`: Photo evidence URL

**Response (200 OK):**
```json
{
  "id": 401,
  "vehicleNumber": "MH01AB1234",
  "driverName": "John",
  "status": "VERIFIED",
  "isVerified": true,
  "verificationScore": 95,
  "verifiedAt": "2024-01-15T14:05:00"
}
```

---

### Check Transport QR Code
**GET** `/transport/check?qrCode=550e8400-e29b-41d4-a716-446655440000`

Check if transport is verified.

**Query Parameters:**
- `qrCode`: QR code to check

**Response (200 OK):**
```json
{
  "id": 401,
  "vehicleNumber": "MH01AB1234",
  "driverName": "John",
  "phoneNumber": "9876543210",
  "status": "VERIFIED",
  "isVerified": true,
  "verificationScore": 95
}
```

---

### Get QR Code Image
**GET** `/transport/qr-image?qrCode=550e8400-e29b-41d4-a716-446655440000`

Get QR code image.

**Query Parameters:**
- `qrCode`: QR code identifier

**Response (200 OK):**
- Content-Type: `image/png`
- Binary PNG image data

---

## 6. Risk Analysis Endpoints

### Analyze Risks
**POST** `/risk/analyze`

Trigger K-Means clustering analysis on incident data.

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
{
  "message": "Risk analysis completed"
}
```

---

### Get Active Risk Clusters
**GET** `/risk/clusters`

Retrieve all active risk clusters.

**Headers:**
```
Authorization: Bearer <token>
```

**Response (200 OK):**
```json
[
  {
    "id": 501,
    "clusterId": 0,
    "centerLatitude": 28.7041,
    "centerLongitude": 77.1025,
    "radius": 0.025,
    "incidentCount": 15,
    "riskLevel": "HIGH",
    "description": "Central business district",
    "isActive": true,
    "createdAt": "2024-01-15T15:00:00"
  }
]
```

---

### Get Nearby Risk Zones
**GET** `/risk/nearby?latitude=28.7041&longitude=77.1025`

Get risk clusters near a location.

**Query Parameters:**
- `latitude`: User latitude
- `longitude`: User longitude

**Response (200 OK):**
```json
[
  {
    "id": 501,
    "clusterId": 0,
    "centerLatitude": 28.7041,
    "centerLongitude": 77.1025,
    "riskLevel": "HIGH",
    "incidentCount": 15
  }
]
```

---

### Get High-Risk Clusters
**GET** `/risk/high-risk`

Retrieve all high-risk areas.

**Response (200 OK):**
```json
[
  {
    "id": 501,
    "clusterId": 0,
    "riskLevel": "HIGH",
    "incidentCount": 15,
    "centerLatitude": 28.7041,
    "centerLongitude": 77.1025
  }
]
```

---

### Get Critical-Risk Clusters
**GET** `/risk/critical-risk`

Retrieve all critical-risk areas.

**Response (200 OK):**
```json
[
  {
    "id": 502,
    "clusterId": 1,
    "riskLevel": "CRITICAL",
    "incidentCount": 25,
    "centerLatitude": 28.6139,
    "centerLongitude": 77.2090
  }
]
```

---

## Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-01-15T15:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid input parameters"
}
```

### 401 Unauthorized
```json
{
  "timestamp": "2024-01-15T15:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid or missing authentication token"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-15T15:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Resource not found"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-01-15T15:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An internal error occurred"
}
```

---

## Rate Limiting (Future)

Future versions will implement rate limiting:
- 100 requests per minute for authenticated users
- 10 requests per minute for public endpoints

---

## Versioning

Current API Version: **1.0.0**

API changes will be versioned as `/api/v2`, `/api/v3`, etc.

