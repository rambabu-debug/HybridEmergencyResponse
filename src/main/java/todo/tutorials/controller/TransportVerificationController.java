package todo.tutorials.controller;

import todo.tutorials.entity.TransportVerification;
import todo.tutorials.service.TransportVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transport")
@CrossOrigin("*")
public class TransportVerificationController {

    @Autowired
    private TransportVerificationService transportVerificationService;

    @PostMapping("/create")
    public ResponseEntity<TransportVerification> createTransport(
            @RequestParam String vehicleNumber,
            @RequestParam String driverName,
            @RequestParam String phoneNumber,
            @RequestParam String licenseNumber) {
        TransportVerification verification = transportVerificationService.createTransportVerification(
                vehicleNumber, driverName, phoneNumber, licenseNumber);
        return ResponseEntity.ok(verification);
    }

    @PostMapping("/verify")
    public ResponseEntity<TransportVerification> verifyTransport(
            @RequestParam String qrCode,
            @RequestParam String photoUrl) {
        TransportVerification verification = transportVerificationService.verifyTransport(qrCode, photoUrl);
        if (verification != null) {
            return ResponseEntity.ok(verification);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/check")
    public ResponseEntity<TransportVerification> checkTransport(@RequestParam String qrCode) {
        var verification = transportVerificationService.getVerificationByQRCode(qrCode);
        return verification.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/qr-image")
    public ResponseEntity<byte[]> getQRImage(@RequestParam String qrCode) {
        try {
            byte[] image = transportVerificationService.generateQRCodeImage(qrCode, 300, 300);
            return ResponseEntity.ok().header("Content-Type", "image/png").body(image);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

