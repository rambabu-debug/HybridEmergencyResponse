package todo.tutorials.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GPSTrackingRequest {
    private Double latitude;
    private Double longitude;
    private Float accuracy;
    private Float speed;
    private Double altitude;
    private String deviceId;
}

