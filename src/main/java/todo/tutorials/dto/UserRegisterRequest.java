package todo.tutorials.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    private String email;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String password;
    private Double latitude;
    private Double longitude;
    private String deviceId;
}

