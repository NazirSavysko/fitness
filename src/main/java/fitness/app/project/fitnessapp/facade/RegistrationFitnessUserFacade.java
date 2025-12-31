package fitness.app.project.fitnessapp.facade;

import fitness.app.project.fitnessapp.dto.RegistrationDTO;
import org.springframework.transaction.annotation.Transactional;

@FunctionalInterface
public interface RegistrationFitnessUserFacade {
    @Transactional
    void register(RegistrationDTO registrationPayload);
}
