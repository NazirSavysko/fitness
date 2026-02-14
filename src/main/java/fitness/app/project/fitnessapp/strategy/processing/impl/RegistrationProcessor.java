package fitness.app.project.fitnessapp.strategy.processing.impl;

import fitness.app.project.fitnessapp.model.User;
import fitness.app.project.fitnessapp.model.enums.VerificationType;
import fitness.app.project.fitnessapp.service.EmailVerificationService;
import fitness.app.project.fitnessapp.service.UserService;
import fitness.app.project.fitnessapp.strategy.processing.VerificationProcessor;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static fitness.app.project.fitnessapp.model.enums.VerificationType.*;

@Component
@AllArgsConstructor
public class RegistrationProcessor implements VerificationProcessor {

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;

    @Override
    public VerificationType getType() {
        return REGISTRATION;
    }

    @Override
    public void process(final String email) {
        final User user = this.userService.getUserByEmail(email);
        user.setEnabled(true);
        userService.saveUser(user);

        this.emailVerificationService.deleteVerificationRecordByEmail(email);
    }
}