package fitness.app.project.fitnessapp.service;

import fitness.app.project.fitnessapp.model.FitnessUser;

public interface RegistrationFitnessUserService {
    FitnessUser registerUser(String email, String password, String name, String surname);
}
