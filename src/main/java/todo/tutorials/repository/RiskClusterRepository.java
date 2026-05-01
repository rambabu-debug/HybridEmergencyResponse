package todo.tutorials.repository;

import todo.tutorials.entity.RiskCluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RiskClusterRepository extends JpaRepository<RiskCluster, Long> {
    List<RiskCluster> findByRiskLevelAndIsActiveTrue(RiskCluster.RiskLevel riskLevel);
    
    @Query("SELECT r FROM RiskCluster r WHERE r.isActive = true ORDER BY r.riskLevel DESC")
    List<RiskCluster> findActiveClustersByRisk();
    
    @Query("SELECT r FROM RiskCluster r WHERE " +
           "SQRT(POWER(r.centerLatitude - :latitude, 2) + POWER(r.centerLongitude - :longitude, 2)) <= r.radius")
    List<RiskCluster> findClustersNear(Double latitude, Double longitude);
}

