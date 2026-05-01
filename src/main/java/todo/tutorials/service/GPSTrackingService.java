package todo.tutorials.service;

import todo.tutorials.dto.GPSTrackingRequest;
import todo.tutorials.entity.GPSTracking;
import todo.tutorials.entity.User;
import todo.tutorials.repository.GPSTrackingRepository;
import todo.tutorials.repository.RiskClusterRepository;
import todo.tutorials.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GPSTrackingService {

    @Autowired
    private GPSTrackingRepository gpsTrackingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RiskClusterRepository riskClusterRepository;

    public GPSTracking trackLocation(Long userId, GPSTrackingRequest request) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return null;
        }

        User user = userOpt.get();
        
        GPSTracking tracking = new GPSTracking();
        tracking.setUser(user);
        tracking.setLatitude(request.getLatitude());
        tracking.setLongitude(request.getLongitude());
        tracking.setAccuracy(request.getAccuracy());
        tracking.setSpeed(request.getSpeed());
        tracking.setAltitude(request.getAltitude());
        tracking.setDeviceId(request.getDeviceId());
        tracking.setTimestamp(LocalDateTime.now());

        // Check if in risk zone
        var nearbyRiskClusters = riskClusterRepository.findClustersNear(request.getLatitude(), request.getLongitude());
        tracking.setIsInRiskZone(!nearbyRiskClusters.isEmpty());
        tracking.setNearbyAlerts(nearbyRiskClusters.size());

        // Update user location
        user.setLatitude(request.getLatitude());
        user.setLongitude(request.getLongitude());
        userRepository.save(user);

        return gpsTrackingRepository.save(tracking);
    }

    public List<GPSTracking> getTrackingHistory(Long userId, int hoursBack) {
        LocalDateTime startTime = LocalDateTime.now().minusHours(hoursBack);
        return gpsTrackingRepository.findByUserIdAndTimestampGreaterThanOrderByTimestampDesc(userId, startTime);
    }

    public GPSTracking getLatestLocation(Long userId) {
        return gpsTrackingRepository.findTopByUserIdOrderByTimestampDesc(userId);
    }
}

