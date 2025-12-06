package fitness.app.project.fitnessapp.service.impl;

import fitness.app.project.fitnessapp.exception.UserExistsException;
import fitness.app.project.fitnessapp.model.FitnessUser;
import fitness.app.project.fitnessapp.model.User;
import fitness.app.project.fitnessapp.repository.FitnessUserRepository;
import fitness.app.project.fitnessapp.service.FitnessUserService;
import fitness.app.project.fitnessapp.service.RegistrationFitnessUserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationFitnessUserServiceImpl implements RegistrationFitnessUserService {

    private final FitnessUserService fitnessUserService;
    private final FitnessUserRepository fitnessUserRepository;

    @SneakyThrows(UserExistsException.class)
    @Override
    public FitnessUser registerUser(final String email, final String password, final String name, final String surname) {
        final User user = this.fitnessUserService.createUser(email, password);

        final FitnessUser fitnessUser = new FitnessUser();
        fitnessUser.setName(name);
        fitnessUser.setSurname(surname);
        fitnessUser.setUserDetails(user);

        return fitnessUserRepository.save(fitnessUser);
    }
}
