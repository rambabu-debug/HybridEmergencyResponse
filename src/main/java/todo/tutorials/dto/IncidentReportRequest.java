package todo.tutorials.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncidentReportRequest {
    private String description;
    private Double latitude;
    private Double longitude;
    private String incidentType;
    private Integer severityLevel;
    private String photoUrl;
}

