package fitness.app.project.fitnessapp.service;

import fitness.app.project.fitnessapp.exception.UserExistsException;
import fitness.app.project.fitnessapp.model.User;

public interface FitnessUserService {

    User createUser(String email, String password) throws UserExistsException;
}
