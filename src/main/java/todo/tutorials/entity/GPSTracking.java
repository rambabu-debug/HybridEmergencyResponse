package todo.tutorials.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "gps_tracking")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GPSTracking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Float accuracy;

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    private Float speed;

    private String deviceId;

    private Double altitude;

    private Boolean isInRiskZone = false;

    private Integer nearbyAlerts = 0;
}

