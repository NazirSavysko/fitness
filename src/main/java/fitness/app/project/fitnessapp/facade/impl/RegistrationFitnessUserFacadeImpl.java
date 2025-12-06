package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.RegistrationDTO;
import fitness.app.project.fitnessapp.exception.InvalidFieldFormatException;
import fitness.app.project.fitnessapp.facade.RegistrationFitnessUserFacade;
import fitness.app.project.fitnessapp.model.FitnessUser;
import fitness.app.project.fitnessapp.service.RegistrationFitnessUserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.InvalidPropertiesFormatException;

@Component
@AllArgsConstructor
public final class RegistrationFitnessUserFacadeImpl implements RegistrationFitnessUserFacade {

    private final static String INVALID_REGISTRATION_DATA = "Invalid registration data";

    private final RegistrationFitnessUserService registrationFitnessUserService;


    @SneakyThrows(InvalidFieldFormatException.class)
    @Override
    public void register(final @NonNull BindingResult bindingResult, final RegistrationDTO registrationPayload)  {
        if (bindingResult.hasErrors()) {
            throw new InvalidFieldFormatException(INVALID_REGISTRATION_DATA);
        }

       final FitnessUser fitnessUser = this.registrationFitnessUserService.registerUser(registrationPayload.email(),
                registrationPayload.password(),
                registrationPayload.name(),
                registrationPayload.surname());
    }
}
