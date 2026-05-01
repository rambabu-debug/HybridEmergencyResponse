package todo.tutorials.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SOSAlertRequest {
    private Double latitude;
    private Double longitude;
    private String triggerType;
    private String encryptedMessage;
    private Boolean isRelayedViaLoRa;
}

