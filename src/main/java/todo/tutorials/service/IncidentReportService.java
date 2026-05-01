package todo.tutorials.service;

import todo.tutorials.dto.IncidentReportRequest;
import todo.tutorials.entity.IncidentReport;
import todo.tutorials.entity.User;
import todo.tutorials.repository.IncidentReportRepository;
import todo.tutorials.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class IncidentReportService {

    @Autowired
    private IncidentReportRepository incidentReportRepository;

    @Autowired
    private UserRepository userRepository;

    public IncidentReport createReport(Long userId, IncidentReportRequest request) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return null;
        }

        IncidentReport report = new IncidentReport();
        report.setUser(userOpt.get());
        report.setDescription(request.getDescription());
        report.setLatitude(request.getLatitude());
        report.setLongitude(request.getLongitude());
        report.setIncidentType(IncidentReport.IncidentType.valueOf(request.getIncidentType().toUpperCase()));
        report.setStatus(IncidentReport.ReportStatus.PENDING);
        report.setSeverityLevel(request.getSeverityLevel());
        report.setPhotoUrl(request.getPhotoUrl());
        report.setCreatedAt(LocalDateTime.now());
        report.setUpdatedAt(LocalDateTime.now());

        return incidentReportRepository.save(report);
    }

    public List<IncidentReport> getReportsByUser(Long userId) {
        return incidentReportRepository.findByUserId(userId);
    }

    public List<IncidentReport> getPendingReports() {
        return incidentReportRepository.findPendingReports();
    }

    public IncidentReport updateReportStatus(Long reportId, String status) {
        Optional<IncidentReport> reportOpt = incidentReportRepository.findById(reportId);
        if (reportOpt.isPresent()) {
            IncidentReport report = reportOpt.get();
            report.setStatus(IncidentReport.ReportStatus.valueOf(status.toUpperCase()));
            report.setUpdatedAt(LocalDateTime.now());
            return incidentReportRepository.save(report);
        }
        return null;
    }

    public List<IncidentReport> getReportsInArea(Double minLat, Double maxLat, Double minLon, Double maxLon, int hoursBack) {
        LocalDateTime startTime = LocalDateTime.now().minusHours(hoursBack);
        return incidentReportRepository.findReportsInArea(minLat, maxLat, minLon, maxLon, startTime);
    }
}

