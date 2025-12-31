package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.RegistrationDTO;
import fitness.app.project.fitnessapp.exception.UserExistsException;
import fitness.app.project.fitnessapp.facade.RegistrationFitnessUserFacade;
import fitness.app.project.fitnessapp.model.FitnessUser;
import fitness.app.project.fitnessapp.model.User;
import fitness.app.project.fitnessapp.service.EmailVerificationService;
import fitness.app.project.fitnessapp.service.FitnessUserService;
import fitness.app.project.fitnessapp.service.UserService;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

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
        if (this.userService.isExistingEmailAndEnableTrue(registrationPayload.email())) {
            throw new UserExistsException(format(USER_FOUND_ERROR, registrationPayload.email()));
        }

        final boolean isExist = this.userService.isUserExist(registrationPayload.email());
        if (!isExist) {
            final User user = this.userService
                    .createUser(registrationPayload.email(), registrationPayload.password());

            final FitnessUser fitnessUser = this.fitnessUserService
                    .createFitnessUser(registrationPayload.name(), registrationPayload.surname());

            fitnessUser.setUserDetails(user);
            this.userService.saveUser(user);
            this.fitnessUserService.saveFitnessUser(fitnessUser);
        }else {
            final User user = this.userService.getUserByEmail(registrationPayload.email());
            user.setPasswordHash(registrationPayload.password());
            this.userService.saveUser(user);

            this.emailVerificationService.deleteVerificationRecordByEmail(registrationPayload.email());
        }

        this.emailVerificationService.sendVerificationEmail(registrationPayload.email());
    }
}
