package fitness.app.project.fitnessapp.strategy.verification.impl;

import fitness.app.project.fitnessapp.model.VerificationType;
import fitness.app.project.fitnessapp.strategy.verification.VerificationRedirectStrategy;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import static fitness.app.project.fitnessapp.model.VerificationType.PASSWORD_RESET;
import static java.lang.String.format;

@Component
public final class ResetPasswordRedirectStrategyImpl implements VerificationRedirectStrategy {
    private static final String RESET_PASSWORD_URL = "redirect:/auth/reset-password?email=%s";
    @Override
    public VerificationType getType() {
        return PASSWORD_RESET;
    }

    @Contract(pure = true)
    @Override
    public @NonNull String getRedirectUrl(final String email, final String code) {
        return format( RESET_PASSWORD_URL, email);
    }
}
