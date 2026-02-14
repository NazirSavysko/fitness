package fitness.app.project.fitnessapp.service.impl;

import fitness.app.project.fitnessapp.exception.PasswordInvalidException;
import fitness.app.project.fitnessapp.exception.UserExistsException;
import fitness.app.project.fitnessapp.model.User;
import fitness.app.project.fitnessapp.repository.UserRepository;
import fitness.app.project.fitnessapp.service.UserService;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static fitness.app.project.fitnessapp.model.enums.Role.ROLE_USER;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Service
@AllArgsConstructor
public final class UserServiceImpl implements UserService {
    private static final String USER_NOT_FOUND_ERROR = "User with email %s not found";
    private static final String PASSWORD_INVALID_ERROR = "Password is invalid.";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isExistingEmailAndEnableTrue(final String email) {
        return this.userRepository.existsByEmailAndEnabledIsTrue(email);
    }

    @Override
    public @NonNull User createUser(final String email, final String password) throws UserExistsException {
       final User user = new User();
       user.setEmail(email);
       user.setPasswordHash(this.passwordEncoder.encode(password));
       user.setCreatedAt(now());
       user.setRole(ROLE_USER);
       user.setEnabled(false);

       return user;
    }

    @Override
    public void saveUser(final User user) {
        this.userRepository.save(user);
    }

    @Override
    public User getUserByEmail(final String email) {
        return this.userRepository.findByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException(format(USER_NOT_FOUND_ERROR, email))
        );
    }

    @Override
    public boolean isUserExist(final String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public void deleteUser(final User user) {
        this.userRepository.delete(user);
    }

    @Override
    public void resetPassword(final String email, final String newPassword, final String confirmPassword) {
        final User user = this.getUserByEmail(email);

        if (passwordEncoder.matches(newPassword, user.getPasswordHash()) || !newPassword.equals(confirmPassword)) {
            throw new PasswordInvalidException(PASSWORD_INVALID_ERROR);
        }

        user.setPasswordHash(this.passwordEncoder.encode(newPassword));

        this.saveUser(user);
    }
}
