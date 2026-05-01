package todo.tutorials.service;

import todo.tutorials.dto.AuthRequest;
import todo.tutorials.dto.AuthResponse;
import todo.tutorials.dto.UserRegisterRequest;
import todo.tutorials.entity.User;
import todo.tutorials.repository.UserRepository;
import todo.tutorials.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public AuthResponse register(UserRegisterRequest request) {
        try {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                return new AuthResponse(null, null, null, null, "Email already exists");
            }

            User user = new User();
            user.setEmail(request.getEmail());
            user.setPhoneNumber(request.getPhoneNumber());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            
            // Set latitude and longitude with defaults if null
            user.setLatitude(request.getLatitude() != null ? request.getLatitude() : 0.0);
            user.setLongitude(request.getLongitude() != null ? request.getLongitude() : 0.0);
            
            user.setDeviceId(request.getDeviceId() != null ? request.getDeviceId() : "device-unknown");
            user.setIsActive(true);
            
            // Check if this is the first user - make them admin
            try {
                long userCount = userRepository.count();
                if (userCount == 0) {
                    user.setRole(User.UserRole.ADMIN);
                } else {
                    user.setRole(User.UserRole.USER);
                }
            } catch (Exception e) {
                user.setRole(User.UserRole.USER);
            }

            User savedUser = userRepository.save(user);

            // Do NOT generate tokens during registration
            // User must log in after registration
            return new AuthResponse(null, null, null, null, "Registration successful! Please log in with your credentials.");
        } catch (Exception e) {
            e.printStackTrace();
            return new AuthResponse(null, null, null, null, "Registration error: " + e.getMessage());
        }
    }

    public AuthResponse login(AuthRequest request) {
        try {
            Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

            if (userOpt.isEmpty()) {
                return new AuthResponse(null, null, null, null, "Invalid credentials");
            }
            
            User user = userOpt.get();
            if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                return new AuthResponse(null, null, null, null, "Invalid credentials");
            }

            String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());
            String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId(), user.getEmail());

            return new AuthResponse(token, refreshToken, user.getId(), user.getEmail(), "Login successful");
        } catch (Exception e) {
            e.printStackTrace();
            return new AuthResponse(null, null, null, null, "Login error: " + e.getMessage());
        }
    }

    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public User updateUserLocation(Long userId, Double latitude, Double longitude) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setLatitude(latitude);
            user.setLongitude(longitude);
            return userRepository.save(user);
        }
        return null;
    }
}

