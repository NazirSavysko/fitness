package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.RegistrationDTO;
import fitness.app.project.fitnessapp.exception.UserExistsException;
import fitness.app.project.fitnessapp.facade.RegistrationUserFacade;
import fitness.app.project.fitnessapp.model.User;
import fitness.app.project.fitnessapp.service.EmailVerificationService;
import fitness.app.project.fitnessapp.service.UserService;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public final class RegistrationUserFacadeImpl implements RegistrationUserFacade {
    private final static String USER_FOUND_ERROR = "User with email already exists";

    private final EmailVerificationService emailVerificationService;
    private final UserService userService;


    @Override
    public void register(final @NonNull RegistrationDTO registrationPayload) {
        if (this.userService.isExistingEmailAndEnableTrue(registrationPayload.email())) {
            throw new UserExistsException(USER_FOUND_ERROR);
        }

        final boolean isExist = this.userService.isUserExist(registrationPayload.email());
        if (!isExist) {
            final User user = this.userService
                    .createUser(registrationPayload.email(), registrationPayload.password());

            user.setFullName(registrationPayload.name() + " " + registrationPayload.surname());
            this.userService.saveUser(user);
        }else {
            final User user = this.userService.getUserByEmail(registrationPayload.email());
            user.setPasswordHash(registrationPayload.password());
            this.userService.saveUser(user);

            this.emailVerificationService.deleteVerificationRecordByEmail(registrationPayload.email());
        }

        this.emailVerificationService.sendVerificationEmail(registrationPayload.email());
    }
}
