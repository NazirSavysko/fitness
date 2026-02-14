package fitness.app.project.fitnessapp.facade;

import fitness.app.project.fitnessapp.model.enums.VerificationType;
import org.springframework.transaction.annotation.Transactional;

@FunctionalInterface
public interface MailFacade {
    @Transactional
    void verifyEmail(String email, String code, VerificationType type);
}
