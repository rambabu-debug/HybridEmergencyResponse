package todo.tutorials.repository;

import todo.tutorials.entity.IncidentReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IncidentReportRepository extends JpaRepository<IncidentReport, Long> {
    List<IncidentReport> findByUserId(Long userId);
    
    List<IncidentReport> findByStatus(IncidentReport.ReportStatus status);
    
    @Query("SELECT i FROM IncidentReport i WHERE i.status = 'PENDING' ORDER BY i.createdAt DESC")
    List<IncidentReport> findPendingReports();
    
    @Query("SELECT i FROM IncidentReport i WHERE i.latitude BETWEEN :minLat AND :maxLat " +
           "AND i.longitude BETWEEN :minLon AND :maxLon " +
           "AND i.createdAt >= :startTime")
    List<IncidentReport> findReportsInArea(Double minLat, Double maxLat, Double minLon, Double maxLon, LocalDateTime startTime);
}

