package fitness.app.project.fitnessapp.repository;

import fitness.app.project.fitnessapp.model.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Integer> {
    Optional<EmailVerification> findByAuthId(Integer authId);
}
