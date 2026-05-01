package todo.tutorials.repository;

import todo.tutorials.entity.GPSTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface GPSTrackingRepository extends JpaRepository<GPSTracking, Long> {
    List<GPSTracking> findByUserId(Long userId);
    
    GPSTracking findTopByUserIdOrderByTimestampDesc(Long userId);
    
    List<GPSTracking> findByUserIdAndTimestampGreaterThanOrderByTimestampDesc(Long userId, LocalDateTime startTime);
}

