package fitness.app.project.fitnessapp.facade;

import fitness.app.project.fitnessapp.dto.RegistrationDTO;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

public interface RegistrationFitnessUserFacade {
    @Transactional
    void register(RegistrationDTO registrationPayload);
}
