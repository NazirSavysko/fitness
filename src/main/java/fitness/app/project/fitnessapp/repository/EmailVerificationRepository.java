package fitness.app.project.fitnessapp.repository;

import aj.org.objectweb.asm.commons.Remapper;
import fitness.app.project.fitnessapp.model.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Integer> {
    Optional<EmailVerification> findByEmail(String email);

    void deleteEmailVerificationByEmail(String email);
}
