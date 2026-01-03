package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.facade.ForgotPasswordFacade;
import fitness.app.project.fitnessapp.service.EmailVerificationService;
import fitness.app.project.fitnessapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public final class ForgotPasswordFacadeImpl implements ForgotPasswordFacade {
    private static final String USER_NOT_FOUND_ERROR = "User with given email does not exist";

    private final UserService userService;
    private final EmailVerificationService emailVerificationService;

    @Override
    public void forgotPassword(final String email) {
        if (!this.userService.isExistingEmailAndEnableTrue(email)) {
           throw new UsernameNotFoundException(USER_NOT_FOUND_ERROR);
        }

        this.emailVerificationService.deleteVerificationRecordByEmail(email);
        this.emailVerificationService.sendVerificationEmail(email);
    }
}
