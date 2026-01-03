package fitness.app.project.fitnessapp.service;

import fitness.app.project.fitnessapp.exception.UserExistsException;
import fitness.app.project.fitnessapp.model.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    boolean isExistingEmailAndEnableTrue(String email);

    User createUser(String email, String password) throws UserExistsException;

    void saveUser(User user);

    User getUserByEmail(String email);

    boolean isUserExist(String email);

    void deleteUser(User user);

    @Transactional
    void resetPassword(String email, String newPassword, String confirmPassword);
}
