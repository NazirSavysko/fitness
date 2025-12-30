package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.facade.MailFacade;
import fitness.app.project.fitnessapp.model.VerificationType;
import fitness.app.project.fitnessapp.service.EmailVerificationService;
import fitness.app.project.fitnessapp.service.UserService;
import fitness.app.project.fitnessapp.strategy.processing.VerificationProcessorFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public final class MailFacadeImpl implements MailFacade {
    private final VerificationProcessorFactory processorFactory;
    private final EmailVerificationService emailVerificationService;
    private final UserService userService;

    @Override
    public void verifyEmail(final String email, final String code, final VerificationType type) {
        this.emailVerificationService.verifyEmailCode(email, code);

        this.processorFactory.process(type, email);
    }
}
