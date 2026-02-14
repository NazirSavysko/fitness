package fitness.app.project.fitnessapp.strategy.verification.impl;

import fitness.app.project.fitnessapp.model.enums.VerificationType;
import fitness.app.project.fitnessapp.strategy.verification.VerificationRedirectStrategy;
import org.springframework.stereotype.Component;

import static fitness.app.project.fitnessapp.model.enums.VerificationType.REGISTRATION;

@Component
public class RegistrationRedirectStrategy implements VerificationRedirectStrategy {
    private static final String REDIRECT_URL_TEMPLATE = "redirect:/auth/login?success";

    @Override
    public VerificationType getType() {
        return REGISTRATION;
    }

    @Override
    public String getRedirectUrl(final String email,final String code) {
        return REDIRECT_URL_TEMPLATE;
    }
}