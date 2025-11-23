package fitness.app.project.fitnessapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/auth/login").permitAll()
                                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilter()
                .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
                .formLogin(form ->
                        form
                                .loginPage("/auth/login")
                                .loginProcessingUrl("/auth/log-in")
                                .usernameParameter("email")
                                .defaultSuccessUrl("/", true)
                                .failureUrl("/auth/login?error")
                                .permitAll()
                )
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(final PasswordEncoder passwordEncoder) {
        return new DaoAuthenticationProvider(username -> new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of((GrantedAuthority) () -> "ADMIN");
            }

            @Override
            public String getPassword() {
                return "1234";
            }

            @Override
            public String getUsername() {
                return "mama";
            }
        });
    }
}
