package todo.tutorials.controller;

import todo.tutorials.dto.GPSTrackingRequest;
import todo.tutorials.entity.GPSTracking;
import todo.tutorials.service.GPSTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gps")
@CrossOrigin("*")
public class GPSTrackingController {

    @Autowired
    private GPSTrackingService gpsTrackingService;

    @PostMapping("/track")
    public ResponseEntity<GPSTracking> trackLocation(@RequestHeader("Authorization") String token, @RequestBody GPSTrackingRequest request) {
        Long userId = extractUserId(token);
        GPSTracking tracking = gpsTrackingService.trackLocation(userId, request);
        if (tracking != null) {
            return ResponseEntity.ok(tracking);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<GPSTracking>> getTrackingHistory(@RequestHeader("Authorization") String token, 
                                                                 @RequestParam(defaultValue = "24") int hours) {
        Long userId = extractUserId(token);
        List<GPSTracking> history = gpsTrackingService.getTrackingHistory(userId, hours);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/latest")
    public ResponseEntity<GPSTracking> getLatestLocation(@RequestHeader("Authorization") String token) {
        Long userId = extractUserId(token);
        GPSTracking tracking = gpsTrackingService.getLatestLocation(userId);
        if (tracking != null) {
            return ResponseEntity.ok(tracking);
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

