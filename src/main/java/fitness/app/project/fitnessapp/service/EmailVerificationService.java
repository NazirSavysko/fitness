package fitness.app.project.fitnessapp.service;

import fitness.app.project.fitnessapp.model.EmailVerification;
import jakarta.persistence.criteria.CriteriaBuilder;

public interface EmailVerificationService {
    void sendVerificationEmail(String email, EmailVerification emailVerification);

    boolean verifyEmailCode(Integer authId, String code);

    void deleteVerificationRecord(EmailVerification emailVerification);

    void saveVerificationRecord(EmailVerification emailVerification);

    EmailVerification createEmailVerificationRecord(Integer authId);

    String generateVerificationCode();
}
