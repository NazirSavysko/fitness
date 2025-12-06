package fitness.app.project.fitnessapp.facade;

import fitness.app.project.fitnessapp.dto.RegistrationDTO;
import org.springframework.validation.BindingResult;

public interface RegistrationFitnessUserFacade {
    void register(BindingResult bindingResult,RegistrationDTO registrationPayload);
}
