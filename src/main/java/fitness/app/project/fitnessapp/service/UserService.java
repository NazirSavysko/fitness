package fitness.app.project.fitnessapp.service;

import fitness.app.project.fitnessapp.exception.UserExistsException;
import fitness.app.project.fitnessapp.model.User;

import java.util.Optional;

public interface UserService {

    boolean isExistingEmailAndEnableTrue(String email);

    User createUser(String email, String password) throws UserExistsException;

    void saveUser(User user);

    User getUserByEmail(String email);

    boolean isUserExist(String email);
}
