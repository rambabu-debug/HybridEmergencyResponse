package todo.tutorials.repository;

import todo.tutorials.entity.TransportVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransportVerificationRepository extends JpaRepository<TransportVerification, Long> {
    Optional<TransportVerification> findByQrCode(String qrCode);
    Optional<TransportVerification> findByVehicleNumber(String vehicleNumber);
}

