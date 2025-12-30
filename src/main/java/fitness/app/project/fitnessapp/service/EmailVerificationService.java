package fitness.app.project.fitnessapp.service;

import fitness.app.project.fitnessapp.model.EmailVerification;

public interface EmailVerificationService {
    void sendVerificationEmail(String email);

    void verifyEmailCode(String email, String code);

    void deleteVerificationRecordByEmail(String email);

    void saveVerificationRecord(EmailVerification emailVerification);

    EmailVerification createEmailVerificationRecord(String email);

    String generateVerificationCode();

    boolean isExistByEmail(String email);
}
