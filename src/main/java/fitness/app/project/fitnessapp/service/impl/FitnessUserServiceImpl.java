package fitness.app.project.fitnessapp.service.impl;

import fitness.app.project.fitnessapp.exception.UserExistsException;
import fitness.app.project.fitnessapp.model.Role;
import fitness.app.project.fitnessapp.model.User;
import fitness.app.project.fitnessapp.repository.UserRepository;
import fitness.app.project.fitnessapp.service.FitnessUserService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static fitness.app.project.fitnessapp.model.Role.ROLE_USER;
import static java.time.LocalDateTime.now;

@Service
@AllArgsConstructor
public class FitnessUserServiceImpl implements FitnessUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User createUser(final String email, final String password) throws UserExistsException {
       if(this.userRepository.findByEmail(email).isPresent()) {
           throw new UserExistsException("User with email " + email + " already exists");
       }

       final User user = new User();
       user.setEmail(email);
       user.setPasswordHash(this.toEncryptedString(password));
       user.setCreatedAt(now());
       user.setRole(ROLE_USER);

        return this.userRepository.save(user);
    }

    private String toEncryptedString(final String password) {
       return this.passwordEncoder.encode(password);
    }
}
