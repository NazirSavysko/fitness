package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.RegistrationDTO;
import fitness.app.project.fitnessapp.exception.UserExistsException;
import fitness.app.project.fitnessapp.facade.RegistrationFitnessUserFacade;
import fitness.app.project.fitnessapp.model.EmailVerification;
import fitness.app.project.fitnessapp.model.FitnessUser;
import fitness.app.project.fitnessapp.model.User;
import fitness.app.project.fitnessapp.service.EmailVerificationService;
import fitness.app.project.fitnessapp.service.UserService;
import fitness.app.project.fitnessapp.service.FitnessUserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Component
@AllArgsConstructor
public final class RegistrationFitnessUserFacadeImpl implements RegistrationFitnessUserFacade {
    private final static String USER_FOUND_ERROR = "User with email %s already exists";

    private final FitnessUserService fitnessUserService;
    private final EmailVerificationService emailVerificationService;
    private final UserService userService;


    @Override
    public void register(final @NonNull RegistrationDTO registrationPayload) {
        if(this.userService.isExistingEmail(registrationPayload.email())) {
           throw new UserExistsException(format(USER_FOUND_ERROR,registrationPayload.email()));
       }

        final User user = this.userService.createUser(registrationPayload.email(), registrationPayload.password());

        final FitnessUser fitnessUser = this.fitnessUserService.createFitnessUser(
                registrationPayload.name(),
                registrationPayload.surname()
        );

        fitnessUser.setUserDetails(user);
        this.userService.saveUser(user);
        this.fitnessUserService.saveFitnessUser(fitnessUser);

        EmailVerification verification = this.emailVerificationService.createEmailVerificationRecord(user.getEmail());

        this.emailVerificationService.saveVerificationRecord(verification);

        this.emailVerificationService.sendVerificationEmail(user.getEmail(), verification);
    }

    @Override
    public void verifyEmail(final String email, final String code) {
        final boolean isVerified = this.emailVerificationService.verifyEmailCode(email, code);

        if (isVerified) {
            final User user = this.userService.getUserByEmail(email);
            user.setEnabled(true);
            this.userService.saveUser(user);

            this.emailVerificationService.deleteVerificationRecordByEmail(email);
        } else {
            throw new IllegalArgumentException("Invalid verification code");
        }
    }
}
