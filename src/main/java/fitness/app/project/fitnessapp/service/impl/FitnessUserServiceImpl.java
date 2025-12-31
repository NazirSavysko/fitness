package fitness.app.project.fitnessapp.service.impl;

import fitness.app.project.fitnessapp.exception.FitnessUserNotFoundException;
import fitness.app.project.fitnessapp.model.FitnessUser;
import fitness.app.project.fitnessapp.repository.FitnessUserRepository;
import fitness.app.project.fitnessapp.service.FitnessUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FitnessUserServiceImpl implements FitnessUserService {
    private static final String FITNESS_USER_NOT_FOUND = "FitnessUser not found";
    private final FitnessUserRepository fitnessUserRepository;

    @Override
    public FitnessUser createFitnessUser(final String name, final String surname) {
        final FitnessUser fitnessUser = new FitnessUser();
        fitnessUser.setName(name);
        fitnessUser.setSurname(surname);

        return fitnessUser;
    }

    @Override
    public void saveFitnessUser(final FitnessUser fitnessUser) {
        this.fitnessUserRepository.save(fitnessUser);
    }

    @Override
    public FitnessUser getFitnessUserByEmail(final String email) {
        return this.fitnessUserRepository.findByUserDetails_Email((email))
                .orElseThrow(() -> new FitnessUserNotFoundException(FITNESS_USER_NOT_FOUND));
    }
}
