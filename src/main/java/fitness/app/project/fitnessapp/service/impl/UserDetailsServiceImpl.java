package fitness.app.project.fitnessapp.service.impl;


import fitness.app.project.fitnessapp.model.User;
import fitness.app.project.fitnessapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

import static java.lang.String.format;

@Service
@AllArgsConstructor
public final class UserDetailsServiceImpl  implements UserDetailsService {

    private static final String NOT_FOUND_ERROR = "User not found with username: %s";

    private final UserRepository userRepository;

    @Contract(pure = true)
    @Override
    public @NonNull UserDetails loadUserByUsername(final @NonNull String username) throws UsernameNotFoundException {

        final User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(format(NOT_FOUND_ERROR, username)));


        return new UserDetails() {
            @Override
            public @NonNull Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(user.getRole());
            }

            @Override
            public @NonNull String getPassword() {
                return user.getPasswordHash();
            }

            @Override
            public @NonNull String getUsername() {
                return user.getEmail();
            }
        };
    }
}
