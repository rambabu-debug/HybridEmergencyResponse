package todo.tutorials.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "sos_alerts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SOSAlert {
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertTriggerType triggerType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime respondedAt;

    private String encryptedMessage;

    private Boolean isRelayedViaLoRa = false;

    private String responderNotes;

    private Long relayNodeId;

    public enum AlertTriggerType {
        SHAKE_DETECTION,
        BUTTON_PRESS,
        MANUAL_SOS,
        AUTOMATIC_ALERT
    }

    public enum AlertStatus {
        ACTIVE,
        ACKNOWLEDGED,
        RESPONDED,
        RESOLVED,
        CANCELLED
    }
}

