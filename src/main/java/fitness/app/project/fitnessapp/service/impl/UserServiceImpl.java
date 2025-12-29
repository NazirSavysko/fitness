package fitness.app.project.fitnessapp.service.impl;

import fitness.app.project.fitnessapp.exception.UserExistsException;
import fitness.app.project.fitnessapp.model.User;
import fitness.app.project.fitnessapp.repository.UserRepository;
import fitness.app.project.fitnessapp.service.UserService;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static fitness.app.project.fitnessapp.model.Role.ROLE_USER;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Service
@AllArgsConstructor
public final class UserServiceImpl implements UserService {
    private static final String USER_NOT_FOUND_ERROR = "User with email %s not found";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isExistingEmail(final String email) {
        return this.userRepository.findByEmail(email).isPresent();
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
                new UserExistsException(format(USER_NOT_FOUND_ERROR, email))
        );
    }
}
