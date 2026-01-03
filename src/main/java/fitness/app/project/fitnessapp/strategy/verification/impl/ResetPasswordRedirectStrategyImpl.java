package fitness.app.project.fitnessapp.strategy.verification.impl;

import fitness.app.project.fitnessapp.model.VerificationType;
import fitness.app.project.fitnessapp.strategy.verification.VerificationRedirectStrategy;
import org.springframework.stereotype.Component;

import static fitness.app.project.fitnessapp.model.VerificationType.PASSWORD_RESET;

@Component
public final class ResetPasswordRedirectStrategyImpl implements VerificationRedirectStrategy {
    private static final String RESET_PASSWORD_URL = "redirect:/auth/login?resetSuccess";
    @Override
    public VerificationType getType() {
        return PASSWORD_RESET;
    }

    @Override
    public String getRedirectUrl(final String email, final String code) {
        return RESET_PASSWORD_URL;
    }
}
