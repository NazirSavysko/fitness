package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.RegistrationDTO;
import fitness.app.project.fitnessapp.facade.FitnessUserFacade;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
public final class FitnessUserFacadeImpl implements FitnessUserFacade {

    @Override
    public void register(final BindingResult bindingResult, final RegistrationDTO registrationPayload) {

    }
}
