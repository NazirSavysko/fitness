package fitness.app.project.fitnessapp.strategy.verification;

import fitness.app.project.fitnessapp.model.enums.VerificationType;

public interface VerificationRedirectStrategy {
    VerificationType getType();
    String getRedirectUrl(String email, String code);
}
