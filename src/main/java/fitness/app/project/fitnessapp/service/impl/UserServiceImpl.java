package fitness.app.project.fitnessapp.service.impl;

import fitness.app.project.fitnessapp.exception.UserExistsException;
import fitness.app.project.fitnessapp.model.User;
import fitness.app.project.fitnessapp.repository.UserRepository;
import fitness.app.project.fitnessapp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static fitness.app.project.fitnessapp.model.Role.ROLE_USER;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean isExistingEmail(final String email) {
        return this.userRepository.findByEmail(email).isPresent();
    }

    @Override
    public User createUser(final String email, final String password) throws UserExistsException {
       final User user = new User();
       user.setEmail(email);
       user.setPasswordHash(this.toEncryptedString(password));
       user.setCreatedAt(now());
       user.setRole(ROLE_USER);

       return user;
    }

    @Override
    public User saveUser(final User user) {
        return this.userRepository.save(user);
    }

    private String toEncryptedString(final String password) {
       return this.passwordEncoder.encode(password);
    }
}
