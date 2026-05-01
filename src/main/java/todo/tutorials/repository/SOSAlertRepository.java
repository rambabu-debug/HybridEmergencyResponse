package todo.tutorials.repository;

import todo.tutorials.entity.SOSAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SOSAlertRepository extends JpaRepository<SOSAlert, Long> {
    List<SOSAlert> findByUserId(Long userId);
    
    List<SOSAlert> findByStatus(SOSAlert.AlertStatus status);
    
    @Query("SELECT s FROM SOSAlert s WHERE s.createdAt >= :startTime AND s.status = 'ACTIVE' ORDER BY s.createdAt DESC")
    List<SOSAlert> findRecentActiveAlerts(LocalDateTime startTime);
    
    @Query("SELECT s FROM SOSAlert s WHERE s.latitude BETWEEN :minLat AND :maxLat " +
           "AND s.longitude BETWEEN :minLon AND :maxLon " +
           "AND s.createdAt >= :startTime")
    List<SOSAlert> findAlertsInArea(Double minLat, Double maxLat, Double minLon, Double maxLon, LocalDateTime startTime);
}

