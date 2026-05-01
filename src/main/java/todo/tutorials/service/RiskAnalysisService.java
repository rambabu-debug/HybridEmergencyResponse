package todo.tutorials.service;

import todo.tutorials.entity.IncidentReport;
import todo.tutorials.entity.RiskCluster;
import todo.tutorials.repository.IncidentReportRepository;
import todo.tutorials.repository.RiskClusterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskAnalysisService {

    @Autowired
    private IncidentReportRepository incidentReportRepository;

    @Autowired
    private RiskClusterRepository riskClusterRepository;

    public void analyzeRisks() {
        List<IncidentReport> incidents = incidentReportRepository.findAll();
        
        if (incidents.size() < 3) {
            return;
        }

        int numClusters = Math.min(5, incidents.size() / 3);
        List<RiskCluster> clusters = ClusteringService.performKMeansClustering(incidents, numClusters);

        for (RiskCluster cluster : clusters) {
            riskClusterRepository.save(cluster);
        }
    }

    public List<RiskCluster> getActiveRiskClusters() {
        return riskClusterRepository.findActiveClustersByRisk();
    }

    public List<RiskCluster> getRiskClustersNear(Double latitude, Double longitude) {
        return riskClusterRepository.findClustersNear(latitude, longitude);
    }

    public List<RiskCluster> getHighRiskClusters() {
        return riskClusterRepository.findByRiskLevelAndIsActiveTrue(RiskCluster.RiskLevel.HIGH);
    }

    public List<RiskCluster> getCriticalRiskClusters() {
        return riskClusterRepository.findByRiskLevelAndIsActiveTrue(RiskCluster.RiskLevel.CRITICAL);
    }
}

