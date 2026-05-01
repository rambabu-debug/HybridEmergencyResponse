// Configuration
const API_BASE_URL = '/api';
let authToken = localStorage.getItem('authToken');
let userId = localStorage.getItem('userId');
let userEmail = localStorage.getItem('userEmail');
let userRole = localStorage.getItem('userRole');
let trackingInterval = null;
let currentUser = null;
let trackingMap = null;
let riskMap = null;
let userLocationMarker = null;
let trackingMarkers = [];
let riskMarkers = [];

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    if (authToken && userId) {
        showMainContent();
        checkAdminStatus();
        loadDashboard();
        initializeMaps();
    } else {
        showAuthSection();
    }

    // Event listeners
    document.getElementById('loginForm').addEventListener('submit', handleLogin);
    document.getElementById('registerForm').addEventListener('submit', handleRegister);
    document.getElementById('sosForm').addEventListener('submit', handleSOSSubmit);
    document.getElementById('incidentForm').addEventListener('submit', handleIncidentSubmit);
});

// ============ MAP FUNCTIONS ============
function initializeMaps() {
    // Initialize Tracking Map
    if (document.getElementById('trackingMap') && !trackingMap) {
        trackingMap = L.map('trackingMap').setView([40.7128, -74.0060], 12);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors',
            maxZoom: 19
        }).addTo(trackingMap);
    }

    // Initialize Risk Map
    if (document.getElementById('riskMap') && !riskMap) {
        riskMap = L.map('riskMap').setView([40.7128, -74.0060], 12);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors',
            maxZoom: 19
        }).addTo(riskMap);
    }
}

// ============ AUTH FUNCTIONS ============
async function handleLogin(e) {
    e.preventDefault();
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;
    const errorDiv = document.getElementById('loginError');

    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        const data = await response.json();
        if (data.token) {
            authToken = data.token;
            userId = data.userId;
            userEmail = data.email;
            localStorage.setItem('authToken', authToken);
            localStorage.setItem('userId', userId);
            localStorage.setItem('userEmail', userEmail);
            currentUser = data;
            showMainContent();
            checkAdminStatus();
            loadDashboard();
            initializeMaps();
            errorDiv.textContent = '';
        } else {
            errorDiv.textContent = data.message || 'Login failed';
            errorDiv.style.display = 'block';
        }
    } catch (error) {
        console.error('Login error:', error);
        errorDiv.textContent = 'Login error: ' + error.message;
        errorDiv.style.display = 'block';
    }
}

async function handleRegister(e) {
    e.preventDefault();

    try {
        const firstName = document.getElementById('firstName').value;
        const lastName = document.getElementById('lastName').value;
        const email = document.getElementById('registerEmail').value;
        const phoneNumber = document.getElementById('phoneNumber').value;
        const password = document.getElementById('registerPassword').value;
        const errorDiv = document.getElementById('registerError');

        // Validate inputs
        if (!firstName || !lastName || !email || !phoneNumber || !password) {
            errorDiv.textContent = 'Please fill in all fields';
            errorDiv.style.display = 'block';
            return;
        }

        // Simple default coordinates
        let latitude = 0, longitude = 0;

        // Try to get location but don't wait for it
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(
                pos => {
                    latitude = pos.coords.latitude;
                    longitude = pos.coords.longitude;
                },
                err => {
                    console.log('Location access denied, using defaults');
                }
            );
        }

        // Prepare payload
        const payload = {
            firstName: firstName.trim(),
            lastName: lastName.trim(),
            email: email.trim(),
            phoneNumber: phoneNumber.trim(),
            password: password,
            latitude: latitude || 0,
            longitude: longitude || 0,
            deviceId: generateDeviceId()
        };

        console.log('Registering with:', payload);

        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        const data = await response.json();

        console.log('Registration response:', data);

        if (data && data.message && data.message.includes('successful')) {
            // Clear form
            document.getElementById('registerForm').reset();
            errorDiv.textContent = '';
            errorDiv.style.display = 'none';

            // Show success message
            console.log('✅ Registration successful! Please log in with your credentials.');
            alert('✅ Registration successful!\n\nPlease log in with your credentials.');

            // Switch to login form
            setTimeout(() => {
                switchTab('login');
            }, 500);
        } else if (!data || data.token === null) {
            // Registration successful but no token (as expected - user must login)
            document.getElementById('registerForm').reset();
            errorDiv.textContent = '';
            errorDiv.style.display = 'none';

            console.log('✅ Registration successful! Please log in with your credentials.');
            alert('✅ Registration successful!\n\nPlease log in with your credentials.');

            // Switch to login form
            setTimeout(() => {
                switchTab('login');
            }, 500);
        } else {
            errorDiv.textContent = data?.message || 'Registration failed. Please try again.';
            errorDiv.style.display = 'block';
            console.error('Registration error:', data);
        }
    } catch (error) {
        console.error('Register error:', error);
        const errorDiv = document.getElementById('registerError');
        errorDiv.textContent = 'Error: ' + error.message;
        errorDiv.style.display = 'block';
    }
}

function logout() {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userId');
    localStorage.removeItem('userEmail');
    authToken = null;
    userId = null;
    userEmail = null;
    showAuthSection();
    clearContent();
}

// ============ UI FUNCTIONS ============
function showAuthSection() {
    document.getElementById('authSection').style.display = 'flex';
    document.getElementById('mainContent').style.display = 'none';
    document.querySelector('.navbar').style.display = 'none';
}

function showMainContent() {
    document.getElementById('authSection').style.display = 'none';
    document.getElementById('mainContent').style.display = 'block';
    document.querySelector('.navbar').style.display = 'block';
}

function switchTab(tab) {
    document.querySelectorAll('.tab-btn').forEach(btn => btn.classList.remove('active'));
    document.querySelectorAll('.auth-form').forEach(form => form.classList.remove('active'));

    event.target.classList.add('active');
    document.getElementById(tab + 'Form').classList.add('active');
}

function switchTransportTab(tab) {
    document.querySelectorAll('.transport-form').forEach(f => f.classList.remove('active'));
    document.getElementById(tab + 'Tab').classList.add('active');
}

function navigateTo(section) {
    document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active'));
    document.getElementById(section).classList.add('active');

    // Initialize maps when navigating to those sections
    setTimeout(() => {
        if (section === 'tracking' && trackingMap) {
            trackingMap.invalidateSize();
        } else if (section === 'risk' && riskMap) {
            riskMap.invalidateSize();
        } else if (section === 'admin') {
            loadAdminDashboard();
        }
    }, 100);
}

// ============ DASHBOARD FUNCTIONS ============
async function loadDashboard() {
    navigateTo('dashboard');
    loadActiveAlerts();
    updateLocation();
    loadRiskZones();
    loadIncidents();
}

async function loadActiveAlerts() {
    try {
        const response = await fetch(`${API_BASE_URL}/sos/active`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });
        const alerts = await response.json();
        document.getElementById('activeAlertsCount').textContent = alerts.length;

        let html = '';
        alerts.slice(0, 5).forEach(alert => {
            html += `
                <div class="alert-item">
                    <h4>🚨 ${alert.triggerType}</h4>
                    <p><strong>Location:</strong> Lat: ${alert.latitude.toFixed(4)}, Lon: ${alert.longitude.toFixed(4)}</p>
                    <p><strong>Status:</strong> <span class="badge badge-warning">${alert.status}</span></p>
                    <p><strong>Time:</strong> ${formatDate(alert.createdAt)}</p>
                </div>
            `;
        });
        document.getElementById('alertsList').innerHTML = html || '<p>✓ No active alerts in your area</p>';
    } catch (error) {
        console.error('Error loading alerts:', error);
    }
}

async function updateLocation() {
    if (!navigator.geolocation) {
        console.log('Geolocation not available');
        return;
    }

    navigator.geolocation.getCurrentPosition(async (position) => {
        const lat = position.coords.latitude;
        const lon = position.coords.longitude;
        const accuracy = position.coords.accuracy;

        document.getElementById('currentLat').textContent = lat.toFixed(4);
        document.getElementById('currentLon').textContent = lon.toFixed(4);
        document.getElementById('currentAccuracy').textContent = accuracy.toFixed(1);
        document.getElementById('currentLocation').textContent = `${lat.toFixed(4)}, ${lon.toFixed(4)}`;
        document.getElementById('lastUpdate').textContent = new Date().toLocaleTimeString();

        // Update map if tracking map exists
        if (trackingMap) {
            trackingMap.setView([lat, lon], 13);
            if (userLocationMarker) {
                userLocationMarker.setLatLng([lat, lon]);
            } else {
                userLocationMarker = L.circleMarker([lat, lon], {
                    radius: 8,
                    fillColor: '#2ecc71',
                    color: '#27ae60',
                    weight: 2,
                    opacity: 1,
                    fillOpacity: 0.8
                }).addTo(trackingMap).bindPopup('Your Location');
            }
        }

        try {
            await fetch(`${API_BASE_URL}/gps/track`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`
                },
                body: JSON.stringify({
                    latitude: lat,
                    longitude: lon,
                    accuracy: accuracy,
                    speed: 0,
                    deviceId: generateDeviceId()
                })
            });
        } catch (error) {
            console.error('Error tracking location:', error);
        }
    });
}

async function loadRiskZones() {
    try {
        const response = await fetch(`${API_BASE_URL}/risk/clusters`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });
        const clusters = await response.json();
        document.getElementById('riskZonesCount').textContent = clusters.length;
    } catch (error) {
        console.error('Error loading risk zones:', error);
    }
}

async function loadIncidents() {
    try {
        const response = await fetch(`${API_BASE_URL}/incident/pending`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });
        const incidents = await response.json();
        document.getElementById('incidentsCount').textContent = incidents.length;
    } catch (error) {
        console.error('Error loading incidents:', error);
    }
}

// ============ SOS FUNCTIONS ============
async function triggerSOSAlert(triggerType) {
    if (!navigator.geolocation) {
        alert('⚠️ Geolocation is required for SOS alert');
        return;
    }

    navigator.geolocation.getCurrentPosition(async (position) => {
        const lat = position.coords.latitude;
        const lon = position.coords.longitude;

        try {
            const response = await fetch(`${API_BASE_URL}/sos/trigger`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`
                },
                body: JSON.stringify({
                    latitude: lat,
                    longitude: lon,
                    triggerType: triggerType,
                    isRelayedViaLoRa: false
                })
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const alert = await response.json();
            alert('🚨 SOS Alert Triggered Successfully!\n\nAlert ID: ' + alert.id + '\nLocation: ' + lat.toFixed(4) + ', ' + lon.toFixed(4));
            loadSOSHistory();
            loadActiveAlerts();
        } catch (error) {
            console.error('Error triggering SOS:', error);
            alert('❌ Error triggering SOS alert: ' + error.message);
        }
    });
}

async function handleSOSSubmit(e) {
    e.preventDefault();
    const message = document.getElementById('sosMessage').value;
    const triggerType = document.getElementById('triggerType').value;

    if (!navigator.geolocation) {
        alert('⚠️ Geolocation is required');
        return;
    }

    navigator.geolocation.getCurrentPosition(async (position) => {
        try {
            const response = await fetch(`${API_BASE_URL}/sos/trigger`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`
                },
                body: JSON.stringify({
                    latitude: position.coords.latitude,
                    longitude: position.coords.longitude,
                    triggerType: triggerType,
                    encryptedMessage: message,
                    isRelayedViaLoRa: false
                })
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            alert('✅ SOS Alert submitted successfully!');
            document.getElementById('sosForm').reset();
            loadSOSHistory();
        } catch (error) {
            console.error('Error:', error);
            alert('❌ Error submitting SOS alert: ' + error.message);
        }
    });
}

async function loadSOSHistory() {
    try {
        const response = await fetch(`${API_BASE_URL}/sos/history`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });
        const alerts = await response.json();

        let html = '';
        alerts.forEach(alert => {
            html += `
                <div class="sos-item">
                    <h4>🚨 ${alert.triggerType}</h4>
                    <p><strong>Location:</strong> ${alert.latitude.toFixed(4)}, ${alert.longitude.toFixed(4)}</p>
                    <p><strong>Status:</strong> <span class="badge badge-${getStatusBadgeClass(alert.status)}">${alert.status}</span></p>
                    <p><strong>Time:</strong> ${formatDate(alert.createdAt)}</p>
                </div>
            `;
        });
        document.getElementById('sosList').innerHTML = html || '<p>No SOS alerts</p>';
    } catch (error) {
        console.error('Error loading SOS history:', error);
    }
}

// ============ TRACKING FUNCTIONS ============
function startTracking() {
    if (!navigator.geolocation) {
        alert('⚠️ Geolocation is required');
        return;
    }

    trackingInterval = setInterval(() => {
        navigator.geolocation.getCurrentPosition(async (position) => {
            await fetch(`${API_BASE_URL}/gps/track`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`
                },
                body: JSON.stringify({
                    latitude: position.coords.latitude,
                    longitude: position.coords.longitude,
                    accuracy: position.coords.accuracy,
                    speed: position.coords.speed || 0,
                    altitude: position.coords.altitude || 0,
                    deviceId: generateDeviceId()
                })
            });

            // Update map
            if (trackingMap) {
                trackingMap.setView([position.coords.latitude, position.coords.longitude], 13);
                if (userLocationMarker) {
                    userLocationMarker.setLatLng([position.coords.latitude, position.coords.longitude]);
                }
            }
        });
    }, 5000); // Track every 5 seconds

    alert('✅ Tracking started! Your location will be updated every 5 seconds.');
}

function stopTracking() {
    if (trackingInterval) {
        clearInterval(trackingInterval);
        trackingInterval = null;
        alert('⏹️ Tracking stopped');
    }
}

async function loadTrackingHistory() {
    try {
        const response = await fetch(`${API_BASE_URL}/gps/history?hours=24`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });
        const history = await response.json();

        let html = '';
        history.forEach(track => {
            html += `
                <div class="tracking-item">
                    <p><strong>Location:</strong> ${track.latitude.toFixed(4)}, ${track.longitude.toFixed(4)}</p>
                    <p><strong>Accuracy:</strong> ${track.accuracy.toFixed(1)}m</p>
                    <p><strong>Time:</strong> ${formatDate(track.timestamp)}</p>
                </div>
            `;
        });
        document.getElementById('trackingList').innerHTML = html || '<p>No tracking history</p>';

        // Add markers to map
        if (trackingMap && history.length > 0) {
            trackingMarkers.forEach(marker => trackingMap.removeLayer(marker));
            trackingMarkers = [];

            history.forEach((track, index) => {
                const marker = L.circleMarker([track.latitude, track.longitude], {
                    radius: 5,
                    fillColor: '#3498db',
                    color: '#2980b9',
                    weight: 1,
                    opacity: 0.7 - (index / history.length) * 0.5,
                    fillOpacity: 0.5
                }).addTo(trackingMap);
                trackingMarkers.push(marker);
            });
        }
    } catch (error) {
        console.error('Error loading tracking history:', error);
    }
}

// ============ INCIDENT FUNCTIONS ============
async function handleIncidentSubmit(e) {
    e.preventDefault();
    const description = document.getElementById('incidentDescription').value;
    const incidentType = document.getElementById('incidentType').value;
    const severityLevel = document.getElementById('severityLevel').value;
    const location = document.getElementById('incidentLocation').value;

    if (!navigator.geolocation) {
        alert('⚠️ Geolocation is required');
        return;
    }

    navigator.geolocation.getCurrentPosition(async (position) => {
        try {
            await fetch(`${API_BASE_URL}/incident/report`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${authToken}`
                },
                body: JSON.stringify({
                    description,
                    latitude: position.coords.latitude,
                    longitude: position.coords.longitude,
                    incidentType,
                    severityLevel: parseInt(severityLevel)
                })
            });
            alert('✅ Incident reported successfully!');
            document.getElementById('incidentForm').reset();
            loadMyIncidents();
        } catch (error) {
            console.error('Error:', error);
            alert('❌ Error reporting incident');
        }
    });
}

async function loadMyIncidents() {
    try {
        const response = await fetch(`${API_BASE_URL}/incident/my-reports`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });
        const reports = await response.json();

        let html = '';
        reports.forEach(report => {
            html += `
                <div class="incident-item">
                    <h4>${report.incidentType}</h4>
                    <p>${report.description}</p>
                    <p><strong>Severity:</strong> <span class="badge badge-${getSeverityBadgeClass(report.severityLevel)}">${getSeverityLabel(report.severityLevel)}</span></p>
                    <p><strong>Status:</strong> <span class="badge badge-${getStatusBadgeClass(report.status)}">${report.status}</span></p>
                    <p><strong>Time:</strong> ${formatDate(report.createdAt)}</p>
                </div>
            `;
        });
        document.getElementById('reportsContainer').innerHTML = html || '<p>No incident reports</p>';
    } catch (error) {
        console.error('Error loading incidents:', error);
    }
}

// ============ TRANSPORT FUNCTIONS ============
async function verifyTransport() {
    const qrCode = document.getElementById('qrCodeInput').value;
    if (!qrCode) {
        alert('Please enter a QR code');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/transport/check?qrCode=${encodeURIComponent(qrCode)}`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        const verification = await response.json();
        let html = '';
        if (response.ok) {
            html = `
                <div class="verification-success">
                    <h4>✓ Transport Verified</h4>
                    <p><strong>Vehicle:</strong> ${verification.vehicleNumber}</p>
                    <p><strong>Driver:</strong> ${verification.driverName}</p>
                    <p><strong>Phone:</strong> ${verification.phoneNumber}</p>
                    <p><strong>License:</strong> ${verification.licenseNumber}</p>
                    <p><strong>Verified:</strong> ${verification.isVerified ? 'Yes ✓' : 'No ✗'}</p>
                </div>
            `;
        } else {
            html = '<div class="verification-error"><h4>✗ Transport Not Found</h4></div>';
        }
        document.getElementById('verificationResult').innerHTML = html;
    } catch (error) {
        console.error('Error verifying transport:', error);
        alert('Error verifying transport');
    }
}

async function createTransportQR() {
    const vehicleNumber = document.getElementById('vehicleNumber').value;
    const driverName = document.getElementById('driverName').value;
    const driverPhone = document.getElementById('driverPhone').value;
    const licenseNumber = document.getElementById('licenseNumber').value;

    if (!vehicleNumber || !driverName || !driverPhone || !licenseNumber) {
        alert('Please fill all fields');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/transport/create?vehicleNumber=${vehicleNumber}&driverName=${driverName}&phoneNumber=${driverPhone}&licenseNumber=${licenseNumber}`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        const verification = await response.json();
        const html = `
            <div class="qr-success">
                <h4>✓ QR Code Generated</h4>
                <p><strong>QR Code:</strong> ${verification.qrCode}</p>
                <p><strong>Vehicle:</strong> ${verification.vehicleNumber}</p>
                <img src="${API_BASE_URL}/transport/qr-image?qrCode=${encodeURIComponent(verification.qrCode)}" alt="QR Code" style="max-width: 300px; margin-top: 1rem;">
            </div>
        `;
        document.getElementById('qrCodeResult').innerHTML = html;
    } catch (error) {
        console.error('Error creating QR code:', error);
        alert('Error creating QR code');
    }
}

// ============ RISK ANALYSIS FUNCTIONS ============
async function loadRiskClusters() {
    try {
        const response = await fetch(`${API_BASE_URL}/risk/clusters`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });
        const clusters = await response.json();

        let html = '';
        clusters.forEach(cluster => {
            html += `
                <div class="risk-item">
                    <h4>Risk Zone ${cluster.clusterId}</h4>
                    <p><strong>Center:</strong> ${cluster.centerLatitude.toFixed(4)}, ${cluster.centerLongitude.toFixed(4)}</p>
                    <p><strong>Radius:</strong> ${cluster.radius.toFixed(2)} km</p>
                    <p><strong>Incidents:</strong> ${cluster.incidentCount}</p>
                    <p><strong>Level:</strong> <span class="badge badge-${getRiskBadgeClass(cluster.riskLevel)}">${cluster.riskLevel}</span></p>
                </div>
            `;
        });
        document.getElementById('riskClustersList').innerHTML = html || '<p>No risk clusters identified</p>';

        // Add markers to risk map
        if (riskMap && clusters.length > 0) {
            riskMarkers.forEach(marker => riskMap.removeLayer(marker));
            riskMarkers = [];

            clusters.forEach(cluster => {
                const riskColor = cluster.riskLevel === 'CRITICAL' ? '#e74c3c' :
                                cluster.riskLevel === 'HIGH' ? '#f39c12' :
                                cluster.riskLevel === 'MEDIUM' ? '#f1c40f' : '#2ecc71';

                const circle = L.circle([cluster.centerLatitude, cluster.centerLongitude], {
                    radius: cluster.radius * 1000,
                    color: riskColor,
                    fillColor: riskColor,
                    fillOpacity: 0.3,
                    weight: 2
                }).addTo(riskMap).bindPopup(`<strong>Risk Zone ${cluster.clusterId}</strong><br/>Level: ${cluster.riskLevel}<br/>Incidents: ${cluster.incidentCount}`);
                riskMarkers.push(circle);
            });

            if (clusters.length > 0) {
                riskMap.fitBounds(L.featureGroup(riskMarkers).getBounds());
            }
        }
    } catch (error) {
        console.error('Error loading risk clusters:', error);
    }
}

// ============ UTILITY FUNCTIONS ============
function generateDeviceId() {
    let deviceId = localStorage.getItem('deviceId');
    if (!deviceId) {
        deviceId = 'device_' + Math.random().toString(36).substr(2, 9);
        localStorage.setItem('deviceId', deviceId);
    }
    return deviceId;
}

function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleString();
}

function getStatusBadgeClass(status) {
    switch(status) {
        case 'ACTIVE': return 'warning';
        case 'RESPONDED': return 'success';
        case 'RESOLVED': return 'info';
        default: return 'info';
    }
}

function getSeverityLabel(level) {
    const labels = ['Low', 'Medium', 'High', 'Critical'];
    return labels[level - 1] || 'Unknown';
}

function getSeverityBadgeClass(level) {
    switch(level) {
        case 1: return 'info';
        case 2: return 'warning';
        case 3: return 'danger';
        case 4: return 'danger';
        default: return 'info';
    }
}

function getRiskBadgeClass(level) {
    switch(level) {
        case 'LOW': return 'success';
        case 'MEDIUM': return 'warning';
        case 'HIGH': return 'danger';
        case 'CRITICAL': return 'danger';
        default: return 'info';
    }
}

function clearContent() {
    document.querySelectorAll('.list-container').forEach(el => el.innerHTML = '');
}

// ============ ADMIN FUNCTIONS ============
function checkAdminStatus() {
    // For now, show admin link if user is defined
    // In production, you can enhance this with actual role check from backend
    if (userId) {
        // Show admin link to all logged-in users (they can try to access it)
        // Access control will be enforced on the backend
        document.getElementById('adminLink').style.display = 'inline-block';
    } else {
        document.getElementById('adminLink').style.display = 'none';
    }
}

function switchAdminTab(tab) {
    document.querySelectorAll('.admin-tab-content').forEach(el => el.classList.remove('active'));
    document.querySelectorAll('.admin-tabs .tab-btn').forEach(btn => btn.classList.remove('active'));

    document.getElementById(tab + 'Tab').classList.add('active');
    event.target.classList.add('active');

    if (tab === 'incidents') {
        loadAdminIncidents();
    } else if (tab === 'sos') {
        loadAdminSOS();
    }
}

async function loadAdminDashboard() {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/dashboard/stats`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.status === 403) {
            alert('You do not have admin access');
            return;
        }

        const stats = await response.json();
        document.getElementById('adminPendingIncidents').textContent = stats.pendingIncidentsCount;
        document.getElementById('adminActiveSOS').textContent = stats.activeSOSCount;
        document.getElementById('adminTotalIncidents').textContent = stats.totalIncidentsCount;
        document.getElementById('adminTotalSOS').textContent = stats.totalSOSCount;

        loadAdminIncidents();
    } catch (error) {
        console.error('Error loading admin dashboard:', error);
    }
}

async function loadAdminIncidents() {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/incidents/pending`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.status === 403) {
            alert('You do not have admin access');
            return;
        }

        const incidents = await response.json();
        let html = '';

        incidents.forEach(incident => {
            html += `
                <div class="admin-incident-item">
                    <h5>📋 ${incident.incidentType}</h5>
                    <p><strong>User:</strong> ${incident.user.firstName} ${incident.user.lastName}</p>
                    <p><strong>Description:</strong> ${incident.description}</p>
                    <p><strong>Location:</strong> ${incident.latitude.toFixed(4)}, ${incident.longitude.toFixed(4)}</p>
                    <p><strong>Severity:</strong> <span class="badge badge-${getSeverityBadgeClass(incident.severityLevel)}">${getSeverityLabel(incident.severityLevel)}</span></p>
                    <p><strong>Current Status:</strong> <span class="badge badge-${getStatusBadgeClass(incident.status)}">${incident.status}</span></p>
                    <p><strong>Created:</strong> ${formatDate(incident.createdAt)}</p>
                    <div class="admin-status-selector">
                        <button class="admin-status-btn" onclick="updateIncidentStatus(${incident.id}, 'ACKNOWLEDGED')">Acknowledge</button>
                        <button class="admin-status-btn" onclick="updateIncidentStatus(${incident.id}, 'INVESTIGATING')">Investigating</button>
                        <button class="admin-status-btn success" onclick="updateIncidentStatus(${incident.id}, 'RESOLVED')">Resolved</button>
                        <button class="admin-status-btn" onclick="updateIncidentStatus(${incident.id}, 'DISMISSED')">Dismiss</button>
                    </div>
                </div>
            `;
        });

        document.getElementById('adminIncidentsList').innerHTML = html || '<p>No pending incidents</p>';
    } catch (error) {
        console.error('Error loading incidents:', error);
    }
}

async function loadAdminSOS() {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/sos/pending`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.status === 403) {
            alert('You do not have admin access');
            return;
        }

        const alerts = await response.json();
        let html = '';

        alerts.forEach(alert => {
            html += `
                <div class="admin-sos-item">
                    <h5>🚨 ${alert.triggerType}</h5>
                    <p><strong>User:</strong> ${alert.user.firstName} ${alert.user.lastName}</p>
                    <p><strong>Location:</strong> ${alert.latitude.toFixed(4)}, ${alert.longitude.toFixed(4)}</p>
                    <p><strong>Current Status:</strong> <span class="badge badge-${getStatusBadgeClass(alert.status)}">${alert.status}</span></p>
                    <p><strong>Created:</strong> ${formatDate(alert.createdAt)}</p>
                    ${alert.responderNotes ? `<p><strong>Notes:</strong> ${alert.responderNotes}</p>` : ''}
                    <div class="admin-status-selector">
                        <button class="admin-status-btn" onclick="updateSOSStatus(${alert.id}, 'ACKNOWLEDGED')">Acknowledge</button>
                        <button class="admin-status-btn success" onclick="updateSOSStatus(${alert.id}, 'RESPONDED')">Respond</button>
                        <button class="admin-status-btn" onclick="updateSOSStatus(${alert.id}, 'RESOLVED')">Resolved</button>
                        <button class="admin-status-btn" onclick="updateSOSStatus(${alert.id}, 'CANCELLED')">Cancel</button>
                    </div>
                </div>
            `;
        });

        document.getElementById('adminSOSList').innerHTML = html || '<p>No active SOS alerts</p>';
    } catch (error) {
        console.error('Error loading SOS alerts:', error);
    }
}

async function updateIncidentStatus(incidentId, newStatus) {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/incidents/${incidentId}/status?status=${newStatus}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            }
        });

        if (response.ok) {
            alert(`✅ Incident status updated to ${newStatus}`);
            loadAdminIncidents();
        } else {
            alert('❌ Error updating incident status');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('❌ Error updating incident: ' + error.message);
    }
}

async function updateSOSStatus(sosId, newStatus) {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/sos/${sosId}/status?status=${newStatus}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}`
            }
        });

        if (response.ok) {
            alert(`✅ SOS status updated to ${newStatus}`);
            loadAdminSOS();
        } else {
            alert('❌ Error updating SOS status');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('❌ Error updating SOS: ' + error.message);
    }
}

// ============ ADMIN DASHBOARD LOADING FUNCTIONS ============
async function loadAdminDashboard() {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/dashboard/stats`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.status === 403) {
            alert('You do not have admin access');
            return;
        }

        const stats = await response.json();
        document.getElementById('adminPendingIncidents').textContent = stats.pendingIncidentsCount;
        document.getElementById('adminActiveSOS').textContent = stats.activeSOSCount;
        document.getElementById('adminTotalIncidents').textContent = stats.totalIncidentsCount;
        document.getElementById('adminTotalSOS').textContent = stats.totalSOSCount;

        loadAdminIncidents();
    } catch (error) {
        console.error('Error loading admin dashboard:', error);
    }
}

async function loadAdminIncidents() {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/incidents/pending`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.status === 403) {
            alert('You do not have admin access');
            return;
        }

        const incidents = await response.json();
        let html = '';

        incidents.forEach(incident => {
            html += `
                <div class="admin-incident-item">
                    <h5>📋 ${incident.incidentType}</h5>
                    <p><strong>User:</strong> ${incident.user.firstName} ${incident.user.lastName}</p>
                    <p><strong>Description:</strong> ${incident.description}</p>
                    <p><strong>Location:</strong> ${incident.latitude.toFixed(4)}, ${incident.longitude.toFixed(4)}</p>
                    <p><strong>Severity:</strong> <span class="badge badge-${getSeverityBadgeClass(incident.severityLevel)}">${getSeverityLabel(incident.severityLevel)}</span></p>
                    <p><strong>Current Status:</strong> <span class="badge badge-${getStatusBadgeClass(incident.status)}">${incident.status}</span></p>
                    <p><strong>Created:</strong> ${formatDate(incident.createdAt)}</p>
                    <div class="admin-status-selector">
                        <button class="admin-status-btn" onclick="updateIncidentStatus(${incident.id}, 'ACKNOWLEDGED')">Acknowledge</button>
                        <button class="admin-status-btn" onclick="updateIncidentStatus(${incident.id}, 'INVESTIGATING')">Investigating</button>
                        <button class="admin-status-btn success" onclick="updateIncidentStatus(${incident.id}, 'RESOLVED')">Resolved</button>
                        <button class="admin-status-btn" onclick="updateIncidentStatus(${incident.id}, 'DISMISSED')">Dismiss</button>
                    </div>
                </div>
            `;
        });

        document.getElementById('adminIncidentsList').innerHTML = html || '<p>No pending incidents</p>';
    } catch (error) {
        console.error('Error loading incidents:', error);
    }
}

async function loadAdminSOS() {
    try {
        const response = await fetch(`${API_BASE_URL}/admin/sos/pending`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (response.status === 403) {
            alert('You do not have admin access');
            return;
        }

        const alerts = await response.json();
        let html = '';

        alerts.forEach(alert => {
            html += `
                <div class="admin-sos-item">
                    <h5>🚨 ${alert.triggerType}</h5>
                    <p><strong>User:</strong> ${alert.user.firstName} ${alert.user.lastName}</p>
                    <p><strong>Location:</strong> ${alert.latitude.toFixed(4)}, ${alert.longitude.toFixed(4)}</p>
                    <p><strong>Current Status:</strong> <span class="badge badge-${getStatusBadgeClass(alert.status)}">${alert.status}</span></p>
                    <p><strong>Created:</strong> ${formatDate(alert.createdAt)}</p>
                    ${alert.responderNotes ? `<p><strong>Notes:</strong> ${alert.responderNotes}</p>` : ''}
                    <div class="admin-status-selector">
                        <button class="admin-status-btn" onclick="updateSOSStatus(${alert.id}, 'ACKNOWLEDGED')">Acknowledge</button>
                        <button class="admin-status-btn success" onclick="updateSOSStatus(${alert.id}, 'RESPONDED')">Respond</button>
                        <button class="admin-status-btn" onclick="updateSOSStatus(${alert.id}, 'RESOLVED')">Resolved</button>
                        <button class="admin-status-btn" onclick="updateSOSStatus(${alert.id}, 'CANCELLED')">Cancel</button>
                    </div>
                </div>
            `;
        });

        document.getElementById('adminSOSList').innerHTML = html || '<p>No active SOS alerts</p>';
    } catch (error) {
        console.error('Error loading SOS alerts:', error);
    }
}

