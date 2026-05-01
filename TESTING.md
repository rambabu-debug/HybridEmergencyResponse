# Testing Guide - Hybrid Emergency Response System

## Quick Test Checklist

This guide provides step-by-step instructions to test all features of the Hybrid Emergency Response System.

## Setup for Testing

1. **Start the application:**
   ```bash
   cd C:\Users\ramba\Downloads\demo\AHybridEmergencyResponseSystem
   ./gradlew bootRun
   ```

2. **Open browser:**
   - Navigate to `http://localhost:8080`
   - You should see the login/register page

3. **Enable Location Services:**
   - Allow browser permission for location access when prompted
   - This is required for all location-based features

---

## Feature Tests

### 1. Authentication Testing

#### 1.1 User Registration
- [ ] Click "Register" tab
- [ ] Fill in:
  - First Name: `John`
  - Last Name: `Doe`
  - Email: `john@test.com`
  - Phone: `+919876543210`
  - Password: `TestPass123`
- [ ] Click "Register"
- [ ] Expected: Dashboard loads, user logged in
- [ ] Verify: Token appears in browser localStorage

**cURL Test:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@test.com",
    "phoneNumber": "9876543210",
    "password": "TestPass123",
    "latitude": 28.7041,
    "longitude": 77.1025,
    "deviceId": "test_device_001"
  }'
```

#### 1.2 User Login
- [ ] Logout (if logged in)
- [ ] Click "Login" tab
- [ ] Enter email: `john@test.com`
- [ ] Enter password: `TestPass123`
- [ ] Click "Login"
- [ ] Expected: Dashboard loads

**cURL Test:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@test.com",
    "password": "TestPass123"
  }'
```

#### 1.3 Invalid Credentials
- [ ] Try login with wrong password
- [ ] Expected: Error message "Invalid credentials"

---

### 2. Dashboard Testing

#### 2.1 Dashboard Load
- [ ] Login successfully
- [ ] Verify dashboard displays:
  - [ ] Active SOS Alerts count (should be 0 initially)
  - [ ] Nearby Risk Zones count
  - [ ] Your Location
  - [ ] Recent Incidents count
  - [ ] Recent Alerts feed

#### 2.2 Location Display
- [ ] Check "Your Location" card
- [ ] Expected: Latitude and longitude displayed (e.g., 28.7041, 77.1025)
- [ ] Click "Update" button
- [ ] Expected: Location refreshes with new timestamp

---

### 3. SOS Alert Testing

#### 3.1 Trigger SOS Alert
- [ ] Navigate to "SOS Alert" section
- [ ] Click the red "TAP TO TRIGGER SOS" button
- [ ] Expected: Alert triggered, ID displayed
- [ ] Check "SOS History" section - new alert appears

**cURL Test:**
```bash
curl -X POST http://localhost:8080/api/sos/trigger \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "latitude": 28.7041,
    "longitude": 77.1025,
    "triggerType": "MANUAL_SOS",
    "encryptedMessage": "Test emergency alert",
    "isRelayedViaLoRa": false
  }'
```

#### 3.2 SOS with Additional Info
- [ ] Fill in emergency details in the form
- [ ] Select trigger type (e.g., "Manual SOS")
- [ ] Add message: `Emergency at location`
- [ ] Click "Submit Additional Info"
- [ ] Expected: Alert created with message

#### 3.3 SOS History
- [ ] Navigate to "SOS History" section
- [ ] Verify all triggered alerts are listed
- [ ] Check status, timestamp, and location

---

### 4. GPS Tracking Testing

#### 4.1 Start Tracking
- [ ] Navigate to "Tracking" section
- [ ] Click "Start Tracking" button
- [ ] Expected: Message "Tracking started!"
- [ ] Location updates every 5 seconds

#### 4.2 View Real-time Location
- [ ] Check current location display
- [ ] Verify: Latitude, Longitude, Accuracy
- [ ] Move to a different location (if possible)
- [ ] Click "Update" and verify location changes

#### 4.3 Tracking History
- [ ] Click "View History" button
- [ ] Expected: List of tracking points with timestamps
- [ ] Verify: Multiple entries from last 24 hours

**cURL Test:**
```bash
curl -X POST http://localhost:8080/api/gps/track \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "latitude": 28.7050,
    "longitude": 77.1030,
    "accuracy": 10.5,
    "speed": 20.0,
    "altitude": 150.0,
    "deviceId": "test_device_001"
  }'
```

#### 4.4 Stop Tracking
- [ ] Click "Stop Tracking" button
- [ ] Expected: Message "Tracking stopped"
- [ ] Verify: No more location updates

---

### 5. Incident Reporting Testing

#### 5.1 Create Incident Report
- [ ] Navigate to "Report an Incident" section
- [ ] Fill form:
  - Type: `THEFT`
  - Severity: `High`
  - Description: `Attempted theft in parking lot`
  - Location: `Parking Lot A`
- [ ] Click "Submit Report"
- [ ] Expected: Report created, appears in "Your Reports"

**cURL Test:**
```bash
curl -X POST http://localhost:8080/api/incident/report \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Attempted theft in parking lot",
    "latitude": 28.7041,
    "longitude": 77.1025,
    "incidentType": "THEFT",
    "severityLevel": 3
  }'
```

#### 5.2 Verify Report Appears
- [ ] Check "Your Reports" section
- [ ] Expected: New report listed with:
  - Type: THEFT
  - Severity: High
  - Status: PENDING
  - Timestamp

#### 5.3 Multiple Incident Types
- [ ] Create reports for different types:
  - [ ] ASSAULT
  - [ ] HARASSMENT
  - [ ] ACCIDENT
  - [ ] SUSPICIOUS_ACTIVITY
- [ ] Verify all appear in history

---

### 6. Transport Verification Testing

#### 6.1 Create Transport QR Code
- [ ] Navigate to "Transport Verification" section
- [ ] Click "Create QR Code" tab
- [ ] Fill in:
  - Vehicle Number: `MH01AB1234`
  - Driver Name: `John Smith`
  - Driver Phone: `+919876543210`
  - License Number: `DL0110989876`
- [ ] Click "Generate QR Code"
- [ ] Expected: QR code displayed with vehicle details

**cURL Test:**
```bash
curl -X POST http://localhost:8080/api/transport/create \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "vehicleNumber": "MH01AB1234",
    "driverName": "John Smith",
    "phoneNumber": "9876543210",
    "licenseNumber": "DL0110989876"
  }'
```

#### 6.2 Verify Transport
- [ ] Click "Verify Transport" tab
- [ ] Enter the QR code from step 6.1
- [ ] Click "Verify"
- [ ] Expected: Green success message with vehicle details

**cURL Test:**
```bash
curl -X GET "http://localhost:8080/api/transport/check?qrCode=YOUR_QR_CODE" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

#### 6.3 Generate QR Image
- [ ] Click "Get QR Image" link (if available)
- [ ] Expected: PNG image displayed
- [ ] Can be scanned with mobile device

---

### 7. Risk Analysis Testing

#### 7.1 Analyze Risks
- [ ] Navigate to "Risk Zones" section
- [ ] Click "Analyze Risks" button
- [ ] Expected: Analysis completes

**cURL Test:**
```bash
curl -X POST http://localhost:8080/api/risk/analyze \
  -H "Authorization: Bearer YOUR_TOKEN"
```

#### 7.2 View Risk Clusters
- [ ] Click "View Risk Clusters" button
- [ ] Expected: List of risk zones with:
  - Cluster ID
  - Center coordinates
  - Radius
  - Incident count
  - Risk level (LOW, MEDIUM, HIGH, CRITICAL)

#### 7.3 Nearby Risk Zones
- [ ] Check nearby risk zones for current location
- [ ] Expected: Risk zones sorted by distance
- [ ] Verify: Risk levels displayed with correct colors

**cURL Test:**
```bash
curl -X GET "http://localhost:8080/api/risk/nearby?latitude=28.7041&longitude=77.1025" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## Integration Tests

### Test Scenario 1: Complete Emergency Response Flow

1. **User Register & Login**
   - [ ] Register new account
   - [ ] Login successfully

2. **Report Initial Location**
   - [ ] Go to Dashboard
   - [ ] Update location

3. **Trigger SOS**
   - [ ] Go to SOS Alert section
   - [ ] Trigger emergency alert
   - [ ] Verify alert appears in history

4. **Start Continuous Tracking**
   - [ ] Go to Tracking section
   - [ ] Start tracking
   - [ ] Wait 30 seconds
   - [ ] Verify multiple tracking points recorded

5. **Report Incident**
   - [ ] Go to Incident Report section
   - [ ] Create incident report with details
   - [ ] Verify report in history

6. **Check Risk Zones**
   - [ ] Go to Risk Zones section
   - [ ] View nearby risk zones
   - [ ] Verify no critical zones nearby

---

### Test Scenario 2: Multiple Users with Conflicting Alerts

1. **Create 3 Test Users:**
   ```
   User1: email1@test.com
   User2: email2@test.com
   User3: email3@test.com
   ```

2. **Each User:**
   - [ ] Login
   - [ ] Trigger SOS alert at different locations
   - [ ] Submit incident report

3. **Check Aggregation:**
   - [ ] View active alerts (should see all 3)
   - [ ] Check area-based alerts
   - [ ] Verify risk clustering

---

## Performance Tests

### Test Duration Performance

1. **Track Location Continuously**
   - [ ] Start tracking
   - [ ] Wait for 5 minutes
   - [ ] Verify 60+ tracking points recorded
   - [ ] No errors in console

2. **Query Large Dataset**
   - [ ] Create 20+ incident reports
   - [ ] Click "View History"
   - [ ] Verify loads in <2 seconds
   - [ ] All records display correctly

3. **Risk Analysis**
   - [ ] With 50+ incidents, run analysis
   - [ ] Verify completes in <5 seconds
   - [ ] Clusters properly identified

---

## Security Tests

### Test Authentication

1. **Token Expiration**
   - [ ] Get a token
   - [ ] Wait for expiration (configured as 24 hours)
   - [ ] Attempt API call with expired token
   - [ ] Expected: 401 Unauthorized

2. **Invalid Token**
   - [ ] Modify token string
   - [ ] Attempt API call
   - [ ] Expected: 401 Unauthorized

3. **Missing Token**
   - [ ] Call protected endpoint without token
   - [ ] Expected: 401 Unauthorized

### Test Input Validation

1. **Empty Fields**
   - [ ] Try register with empty email
   - [ ] Expected: Error message

2. **Invalid Email**
   - [ ] Try register with invalid email format
   - [ ] Expected: Validation error

3. **Invalid Coordinates**
   - [ ] Submit tracking with latitude > 90
   - [ ] Expected: Rejected or normalized

---

## Browser Compatibility Tests

- [ ] Chrome/Edge
- [ ] Firefox
- [ ] Safari (if available)
- [ ] Mobile Browser

**Verify:**
- [ ] Responsive design works
- [ ] Geolocation works
- [ ] LocalStorage works
- [ ] No console errors

---

## Error Handling Tests

### Test Error Scenarios

1. **Database Disconnection**
   - [ ] Stop database
   - [ ] Try API call
   - [ ] Expected: Graceful error message

2. **Invalid Location**
   - [ ] Submit latitude > 90
   - [ ] Expected: Validation error

3. **Duplicate Email**
   - [ ] Try registering with same email
   - [ ] Expected: "Email already exists"

4. **Missing Required Fields**
   - [ ] Submit form with missing fields
   - [ ] Expected: Required field validation

---

## Test Results Summary

Create a test results file (`TEST_RESULTS.txt`):

```
Test Execution Date: [Date]
Tested By: [Name]
System: [OS/Browser]

FEATURES TESTED:
[ ] Authentication (Register/Login)
[ ] Dashboard
[ ] SOS Alerts
[ ] GPS Tracking
[ ] Incident Reporting
[ ] Transport Verification
[ ] Risk Analysis

BUGS FOUND:
[List any bugs]

PERFORMANCE:
- Average Response Time: [ms]
- Database Query Time: [ms]
- Frontend Load Time: [ms]

OVERALL STATUS:
[ ] PASS
[ ] PASS WITH MINOR ISSUES
[ ] FAIL

NOTES:
[Additional notes]
```

---

## Continuous Testing

After each deployment:

1. **Smoke Test (5 min)**
   - Register user
   - Login
   - Submit alert
   - Trigger tracking

2. **Full Test (30 min)**
   - All features
   - Error scenarios
   - Performance checks

3. **Load Test (Optional)**
   - Use Apache JMeter
   - 100+ concurrent users
   - Verify system stability

