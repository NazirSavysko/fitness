package fitness.app.project.fitnessapp.service;

import fitness.app.project.fitnessapp.model.FitnessUser;

public interface FitnessUserService {
    FitnessUser createFitnessUser(String name, String surname);

    void saveFitnessUser(FitnessUser fitnessUser);

    FitnessUser getFitnessUserByEmail(String email);
}
