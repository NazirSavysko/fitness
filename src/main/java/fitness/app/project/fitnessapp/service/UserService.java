package fitness.app.project.fitnessapp.service;

import fitness.app.project.fitnessapp.annotations.ValidEmail;
import fitness.app.project.fitnessapp.exception.UserExistsException;
import fitness.app.project.fitnessapp.model.User;

public interface UserService {

    boolean isExistingEmail(String email);

    User createUser(String email, String password) throws UserExistsException;

    User saveUser(User user);
}
