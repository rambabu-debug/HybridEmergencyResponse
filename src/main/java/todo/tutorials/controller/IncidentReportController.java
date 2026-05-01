package todo.tutorials.controller;

import todo.tutorials.dto.IncidentReportRequest;
import todo.tutorials.entity.IncidentReport;
import todo.tutorials.service.IncidentReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incident")
@CrossOrigin("*")
public class IncidentReportController {

    @Autowired
    private IncidentReportService incidentReportService;

    @PostMapping("/report")
    public ResponseEntity<IncidentReport> createReport(@RequestHeader("Authorization") String token, 
                                                        @RequestBody IncidentReportRequest request) {
        Long userId = extractUserId(token);
        IncidentReport report = incidentReportService.createReport(userId, request);
        if (report != null) {
            return ResponseEntity.ok(report);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/my-reports")
    public ResponseEntity<List<IncidentReport>> getMyReports(@RequestHeader("Authorization") String token) {
        Long userId = extractUserId(token);
        List<IncidentReport> reports = incidentReportService.getReportsByUser(userId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<IncidentReport>> getPendingReports() {
        List<IncidentReport> reports = incidentReportService.getPendingReports();
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/area")
    public ResponseEntity<List<IncidentReport>> getReportsInArea(
            @RequestParam Double minLat,
            @RequestParam Double maxLat,
            @RequestParam Double minLon,
            @RequestParam Double maxLon,
            @RequestParam(defaultValue = "24") int hours) {
        List<IncidentReport> reports = incidentReportService.getReportsInArea(minLat, maxLat, minLon, maxLon, hours);
        return ResponseEntity.ok(reports);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<IncidentReport> updateReportStatus(@PathVariable Long id, @RequestParam String status) {
        IncidentReport report = incidentReportService.updateReportStatus(id, status);
        if (report != null) {
            return ResponseEntity.ok(report);
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

