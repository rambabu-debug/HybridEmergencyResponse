package todo.tutorials.service;

import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.DoublePoint;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import todo.tutorials.entity.IncidentReport;
import todo.tutorials.entity.RiskCluster;

import java.util.ArrayList;
import java.util.List;

public class ClusteringService {

    public static List<RiskCluster> performKMeansClustering(List<IncidentReport> incidents, int numClusters) {
        if (incidents.isEmpty()) {
            return new ArrayList<>();
        }

        List<DoublePoint> points = new ArrayList<>();
        for (IncidentReport incident : incidents) {
            points.add(new DoublePoint(new double[]{incident.getLatitude(), incident.getLongitude()}));
        }

        KMeansPlusPlusClusterer<DoublePoint> clusterer = new KMeansPlusPlusClusterer<>(numClusters, 1000);
        List<CentroidCluster<DoublePoint>> clusters = clusterer.cluster(points);

        List<RiskCluster> riskClusters = new ArrayList<>();

        for (int i = 0; i < clusters.size(); i++) {
            CentroidCluster<DoublePoint> cluster = clusters.get(i);
            double[] center = cluster.getCenter().getPoint();

            double maxDistance = calculateMaxDistance(center, cluster.getPoints());

            RiskCluster riskCluster = new RiskCluster();
            riskCluster.setClusterId(i);
            riskCluster.setCenterLatitude(center[0]);
            riskCluster.setCenterLongitude(center[1]);
            riskCluster.setRadius(maxDistance);
            riskCluster.setIncidentCount(cluster.getPoints().size());
            riskCluster.setRiskLevel(determineRiskLevel(cluster.getPoints().size()));
            riskCluster.setIsActive(true);

            riskClusters.add(riskCluster);
        }

        return riskClusters;
    }

    private static double calculateMaxDistance(double[] center, List<DoublePoint> points) {
        double maxDistance = 0;
        for (DoublePoint point : points) {
            double[] coords = point.getPoint();
            double distance = Math.sqrt(
                    Math.pow(coords[0] - center[0], 2) +
                    Math.pow(coords[1] - center[1], 2)
            );
            maxDistance = Math.max(maxDistance, distance);
        }
        return maxDistance;
    }

    private static RiskCluster.RiskLevel determineRiskLevel(int incidentCount) {
        if (incidentCount > 20) {
            return RiskCluster.RiskLevel.CRITICAL;
        } else if (incidentCount > 10) {
            return RiskCluster.RiskLevel.HIGH;
        } else if (incidentCount > 5) {
            return RiskCluster.RiskLevel.MEDIUM;
        }
        return RiskCluster.RiskLevel.LOW;
    }
}

