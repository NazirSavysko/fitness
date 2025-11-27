package fitness.app.project.fitnessapp.facade;

import fitness.app.project.fitnessapp.dto.RegistrationDTO;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

public interface FitnessUserFacade {
    void register(BindingResult bindingResult,RegistrationDTO registrationPayload);
}
