package todo.tutorials.service;

import todo.tutorials.dto.SOSAlertRequest;
import todo.tutorials.entity.SOSAlert;
import todo.tutorials.entity.User;
import todo.tutorials.repository.SOSAlertRepository;
import todo.tutorials.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SOSAlertService {

    @Autowired
    private SOSAlertRepository sosAlertRepository;

    @Autowired
    private UserRepository userRepository;

    public SOSAlert createAlert(Long userId, SOSAlertRequest request) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return null;
        }

        SOSAlert alert = new SOSAlert();
        alert.setUser(userOpt.get());
        alert.setLatitude(request.getLatitude());
        alert.setLongitude(request.getLongitude());
        alert.setTriggerType(SOSAlert.AlertTriggerType.valueOf(request.getTriggerType().toUpperCase()));
        alert.setStatus(SOSAlert.AlertStatus.ACTIVE);
        alert.setEncryptedMessage(request.getEncryptedMessage());
        alert.setIsRelayedViaLoRa(request.getIsRelayedViaLoRa() != null && request.getIsRelayedViaLoRa());
        alert.setCreatedAt(LocalDateTime.now());

        return sosAlertRepository.save(alert);
    }

    public List<SOSAlert> getAlertsByUser(Long userId) {
        return sosAlertRepository.findByUserId(userId);
    }

    public List<SOSAlert> getRecentActiveAlerts(int minutesBack) {
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(minutesBack);
        return sosAlertRepository.findRecentActiveAlerts(startTime);
    }

    public SOSAlert updateAlertStatus(Long alertId, String status) {
        Optional<SOSAlert> alertOpt = sosAlertRepository.findById(alertId);
        if (alertOpt.isPresent()) {
            SOSAlert alert = alertOpt.get();
            alert.setStatus(SOSAlert.AlertStatus.valueOf(status.toUpperCase()));
            if (status.equalsIgnoreCase("RESPONDED")) {
                alert.setRespondedAt(LocalDateTime.now());
            }
            return sosAlertRepository.save(alert);
        }
        return null;
    }

    public List<SOSAlert> getAlertsInArea(Double minLat, Double maxLat, Double minLon, Double maxLon, int minutesBack) {
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(minutesBack);
        return sosAlertRepository.findAlertsInArea(minLat, maxLat, minLon, maxLon, startTime);
    }
}

