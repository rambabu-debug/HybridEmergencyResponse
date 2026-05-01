package todo.tutorials.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import todo.tutorials.entity.TransportVerification;
import todo.tutorials.repository.TransportVerificationRepository;
import todo.tutorials.security.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransportVerificationService {

    @Autowired
    private TransportVerificationRepository transportVerificationRepository;

    public TransportVerification createTransportVerification(String vehicleNumber, String driverName, String phoneNumber, String licenseNumber) {
        String qrCode = UUID.randomUUID().toString();
        
        TransportVerification verification = new TransportVerification();
        verification.setQrCode(qrCode);
        verification.setVehicleNumber(vehicleNumber);
        verification.setDriverName(driverName);
        verification.setPhoneNumber(phoneNumber);
        verification.setLicenseNumber(licenseNumber);
        verification.setStatus(TransportVerification.VerificationStatus.PENDING);
        verification.setIsVerified(false);
        verification.setCreatedAt(LocalDateTime.now());

        return transportVerificationRepository.save(verification);
    }

    public TransportVerification verifyTransport(String qrCode, String photoUrl) {
        Optional<TransportVerification> verificationOpt = transportVerificationRepository.findByQrCode(qrCode);
        if (verificationOpt.isPresent()) {
            TransportVerification verification = verificationOpt.get();
            verification.setIsVerified(true);
            verification.setStatus(TransportVerification.VerificationStatus.VERIFIED);
            verification.setVerifiedAt(LocalDateTime.now());
            verification.setPhotoUrl(photoUrl);
            verification.setVerificationScore(95);
            return transportVerificationRepository.save(verification);
        }
        return null;
    }

    public Optional<TransportVerification> getVerificationByQRCode(String qrCode) {
        return transportVerificationRepository.findByQrCode(qrCode);
    }

    public Optional<TransportVerification> getVerificationByVehicle(String vehicleNumber) {
        return transportVerificationRepository.findByVehicleNumber(vehicleNumber);
    }

    public byte[] generateQRCodeImage(String qrCode, int width, int height) throws WriterException, IOException {
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix bitMatrix = writer.encode(qrCode, BarcodeFormat.QR_CODE, width, height);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);
        return baos.toByteArray();
    }
}

