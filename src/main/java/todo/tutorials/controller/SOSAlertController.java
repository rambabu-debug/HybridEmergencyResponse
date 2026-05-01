package todo.tutorials.controller;

import todo.tutorials.dto.SOSAlertRequest;
import todo.tutorials.entity.SOSAlert;
import todo.tutorials.service.SOSAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sos")
@CrossOrigin("*")
public class SOSAlertController {

    @Autowired
    private SOSAlertService sosAlertService;

    @PostMapping("/trigger")
    public ResponseEntity<SOSAlert> triggerSOS(@RequestHeader("Authorization") String token, @RequestBody SOSAlertRequest request) {
        Long userId = extractUserId(token);
        SOSAlert alert = sosAlertService.createAlert(userId, request);
        if (alert != null) {
            return ResponseEntity.ok(alert);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<SOSAlert>> getAlertHistory(@RequestHeader("Authorization") String token) {
        Long userId = extractUserId(token);
        List<SOSAlert> alerts = sosAlertService.getAlertsByUser(userId);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/active")
    public ResponseEntity<List<SOSAlert>> getActiveAlerts() {
        List<SOSAlert> alerts = sosAlertService.getRecentActiveAlerts(60);
        return ResponseEntity.ok(alerts);
    }

    @GetMapping("/area")
    public ResponseEntity<List<SOSAlert>> getAlertsInArea(
            @RequestParam Double minLat,
            @RequestParam Double maxLat,
            @RequestParam Double minLon,
            @RequestParam Double maxLon) {
        List<SOSAlert> alerts = sosAlertService.getAlertsInArea(minLat, maxLat, minLon, maxLon, 60);
        return ResponseEntity.ok(alerts);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<SOSAlert> updateAlertStatus(@PathVariable Long id, @RequestParam String status) {
        SOSAlert alert = sosAlertService.updateAlertStatus(id, status);
        if (alert != null) {
            return ResponseEntity.ok(alert);
        }
        return ResponseEntity.notFound().build();
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

