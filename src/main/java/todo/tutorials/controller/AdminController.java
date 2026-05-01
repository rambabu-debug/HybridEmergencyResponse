package todo.tutorials.controller;

import todo.tutorials.entity.IncidentReport;
import todo.tutorials.entity.SOSAlert;
import todo.tutorials.entity.User;
import todo.tutorials.repository.IncidentReportRepository;
import todo.tutorials.repository.SOSAlertRepository;
import todo.tutorials.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class
AdminController {

    @Autowired
    private IncidentReportRepository incidentReportRepository;

    @Autowired
    private SOSAlertRepository sosAlertRepository;

    @Autowired
    private UserRepository userRepository;

    // ============ ADMIN VERIFICATION ============
    private boolean isAdmin(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.isPresent() && user.get().getRole() == User.UserRole.ADMIN;
    }

    // ============ INCIDENT MANAGEMENT ============
    @GetMapping("/incidents/pending")
    public ResponseEntity<List<IncidentReport>> getPendingIncidents(@RequestHeader("Authorization") String token) {
        Long userId = extractUserId(token);
        if (!isAdmin(userId)) {
            return ResponseEntity.status(403).build();
        }
        List<IncidentReport> incidents = incidentReportRepository.findByStatus(IncidentReport.ReportStatus.PENDING);
        return ResponseEntity.ok(incidents);
    }

    @GetMapping("/incidents/all")
    public ResponseEntity<List<IncidentReport>> getAllIncidents(@RequestHeader("Authorization") String token) {
        Long userId = extractUserId(token);
        if (!isAdmin(userId)) {
            return ResponseEntity.status(403).build();
        }
        List<IncidentReport> incidents = incidentReportRepository.findAll();
        return ResponseEntity.ok(incidents);
    }

    @PutMapping("/incidents/{id}/status")
    public ResponseEntity<IncidentReport> updateIncidentStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String notes) {
        
        Long userId = extractUserId(token);
        if (!isAdmin(userId)) {
            return ResponseEntity.status(403).build();
        }

        try {
            Optional<IncidentReport> incidentOpt = incidentReportRepository.findById(id);
            if (incidentOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            IncidentReport incident = incidentOpt.get();
            IncidentReport.ReportStatus newStatus = IncidentReport.ReportStatus.valueOf(status.toUpperCase());
            incident.setStatus(newStatus);
            incident.setUpdatedAt(LocalDateTime.now());
            
            IncidentReport updated = incidentReportRepository.save(incident);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/incidents/{id}/acknowledge")
    public ResponseEntity<IncidentReport> acknowledgeIncident(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        
        Long userId = extractUserId(token);
        if (!isAdmin(userId)) {
            return ResponseEntity.status(403).build();
        }

        Optional<IncidentReport> incidentOpt = incidentReportRepository.findById(id);
        if (incidentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        IncidentReport incident = incidentOpt.get();
        incident.setStatus(IncidentReport.ReportStatus.ACKNOWLEDGED);
        incident.setUpdatedAt(LocalDateTime.now());
        
        IncidentReport updated = incidentReportRepository.save(incident);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/incidents/{id}/resolve")
    public ResponseEntity<IncidentReport> resolveIncident(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        
        Long userId = extractUserId(token);
        if (!isAdmin(userId)) {
            return ResponseEntity.status(403).build();
        }

        Optional<IncidentReport> incidentOpt = incidentReportRepository.findById(id);
        if (incidentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        IncidentReport incident = incidentOpt.get();
        incident.setStatus(IncidentReport.ReportStatus.RESOLVED);
        incident.setUpdatedAt(LocalDateTime.now());
        
        IncidentReport updated = incidentReportRepository.save(incident);
        return ResponseEntity.ok(updated);
    }

    // ============ SOS ALERT MANAGEMENT ============
    @GetMapping("/sos/pending")
    public ResponseEntity<List<SOSAlert>> getPendingSOS(@RequestHeader("Authorization") String token) {
        Long userId = extractUserId(token);
        if (!isAdmin(userId)) {
            return ResponseEntity.status(403).build();
        }
        List<SOSAlert> alerts = sosAlertRepository.findByStatus(SOSAlert.AlertStatus.ACTIVE);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/sos/all")
    public ResponseEntity<List<SOSAlert>> getAllSOS(@RequestHeader("Authorization") String token) {
        Long userId = extractUserId(token);
        if (!isAdmin(userId)) {
            return ResponseEntity.status(403).build();
        }
        List<SOSAlert> alerts = sosAlertRepository.findAll();
        return ResponseEntity.ok(alerts);
    }

    @PutMapping("/sos/{id}/status")
    public ResponseEntity<SOSAlert> updateSOSStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String notes) {
        
        Long userId = extractUserId(token);
        if (!isAdmin(userId)) {
            return ResponseEntity.status(403).build();
        }

        try {
            Optional<SOSAlert> alertOpt = sosAlertRepository.findById(id);
            if (alertOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            SOSAlert alert = alertOpt.get();
            SOSAlert.AlertStatus newStatus = SOSAlert.AlertStatus.valueOf(status.toUpperCase());
            alert.setStatus(newStatus);
            if (newStatus == SOSAlert.AlertStatus.RESPONDED) {
                alert.setRespondedAt(LocalDateTime.now());
            }
            if (notes != null) {
                alert.setResponderNotes(notes);
            }
            
            SOSAlert updated = sosAlertRepository.save(alert);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/sos/{id}/respond")
    public ResponseEntity<SOSAlert> respondToSOS(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestParam(required = false) String notes) {
        
        Long userId = extractUserId(token);
        if (!isAdmin(userId)) {
            return ResponseEntity.status(403).build();
        }

        Optional<SOSAlert> alertOpt = sosAlertRepository.findById(id);
        if (alertOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        SOSAlert alert = alertOpt.get();
        alert.setStatus(SOSAlert.AlertStatus.RESPONDED);
        alert.setRespondedAt(LocalDateTime.now());
        if (notes != null) {
            alert.setResponderNotes(notes);
        }
        
        SOSAlert updated = sosAlertRepository.save(alert);
        return ResponseEntity.ok(updated);
    }

    // ============ DASHBOARD STATS ============
    @GetMapping("/dashboard/stats")
    public ResponseEntity<?> getDashboardStats(@RequestHeader("Authorization") String token) {
        Long userId = extractUserId(token);
        if (!isAdmin(userId)) {
            return ResponseEntity.status(403).build();
        }

        List<IncidentReport> pendingIncidents = incidentReportRepository.findByStatus(IncidentReport.ReportStatus.PENDING);
        List<SOSAlert> activeSOS = sosAlertRepository.findByStatus(SOSAlert.AlertStatus.ACTIVE);
        List<IncidentReport> allIncidents = incidentReportRepository.findAll();
        List<SOSAlert> allSOS = sosAlertRepository.findAll();

        return ResponseEntity.ok(new Object() {
            public final int pendingIncidentsCount = pendingIncidents.size();
            public final int activeSOSCount = activeSOS.size();
            public final int totalIncidentsCount = allIncidents.size();
            public final int totalSOSCount = allSOS.size();
            public final long resolvedIncidentsCount = allIncidents.stream()
                .filter(i -> i.getStatus() == IncidentReport.ReportStatus.RESOLVED).count();
            public final long respondedSOSCount = allSOS.stream()
                .filter(s -> s.getStatus() == SOSAlert.AlertStatus.RESPONDED).count();
        });
    }

    private Long extractUserId(String token) {
        try {
            String[] parts = token.replace("Bearer ", "").split("\\.");
            return Long.parseLong(parts[0]);
        } catch (Exception e) {
            return 1L;
        }
    }
}

