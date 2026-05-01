package todo.tutorials.controller;

import todo.tutorials.entity.RiskCluster;
import todo.tutorials.service.RiskAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk")
@CrossOrigin("*")
public class RiskAnalysisController {

    @Autowired
    private RiskAnalysisService riskAnalysisService;

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeRisks() {
        riskAnalysisService.analyzeRisks();
        return ResponseEntity.ok("Risk analysis completed");
    }

    @GetMapping("/clusters")
    public ResponseEntity<List<RiskCluster>> getActiveClusters() {
        List<RiskCluster> clusters = riskAnalysisService.getActiveRiskClusters();
        return ResponseEntity.ok(clusters);
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<RiskCluster>> getNearby(@RequestParam Double latitude, @RequestParam Double longitude) {
        List<RiskCluster> clusters = riskAnalysisService.getRiskClustersNear(latitude, longitude);
        return ResponseEntity.ok(clusters);
    }

    @GetMapping("/high-risk")
    public ResponseEntity<List<RiskCluster>> getHighRisk() {
        List<RiskCluster> clusters = riskAnalysisService.getHighRiskClusters();
        return ResponseEntity.ok(clusters);
    }

    @GetMapping("/critical-risk")
    public ResponseEntity<List<RiskCluster>> getCriticalRisk() {
        List<RiskCluster> clusters = riskAnalysisService.getCriticalRiskClusters();
        return ResponseEntity.ok(clusters);
    }
}

