package fitness.app.project.fitnessapp.strategy.processing.impl;

import fitness.app.project.fitnessapp.model.enums.VerificationType;
import fitness.app.project.fitnessapp.service.EmailVerificationService;
import fitness.app.project.fitnessapp.strategy.processing.VerificationProcessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static fitness.app.project.fitnessapp.model.enums.VerificationType.PASSWORD_RESET;

@Component
@AllArgsConstructor
public class ResetPasswordProcessorImpl implements VerificationProcessor {

    private final EmailVerificationService emailVerificationService;

    @Override
    public VerificationType getType() {
        return PASSWORD_RESET;
    }

    @Override
    public void process(final String email) {
        this.emailVerificationService.deleteVerificationRecordByEmail(email);
    }
}
