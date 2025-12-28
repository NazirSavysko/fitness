package fitness.app.project.fitnessapp.service;

import fitness.app.project.fitnessapp.model.EmailVerification;
import org.jspecify.annotations.NonNull;

public interface EmailVerificationService {
    void sendVerificationEmail(String email, EmailVerification emailVerification);

    boolean verifyEmailCode(String email, String code);

    void deleteVerificationRecordByEmail(String email);

    void saveVerificationRecord(EmailVerification emailVerification);

    EmailVerification createEmailVerificationRecord(String email);

    String generateVerificationCode();
}
