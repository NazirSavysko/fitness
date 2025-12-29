package fitness.app.project.fitnessapp.security.provider;

import fitness.app.project.fitnessapp.dto.LoginDTO;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.*;


import static java.util.Objects.requireNonNull;


public final class DaoAuthenticationProviderWithValidation extends DaoAuthenticationProvider {
    private final Validator validator;

    public DaoAuthenticationProviderWithValidation(final UserDetailsService userDetailsService, final Validator validator) {
        super(userDetailsService);
        this.validator = validator;

    }

    @Override
    public @NonNull Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        final LoginDTO loginDTO = new LoginDTO(authentication.getName(), requireNonNull(authentication.getCredentials()).toString());
        this.validator.validate(loginDTO,new SimpleErrors(loginDTO));

        final Errors errors = new BeanPropertyBindingResult(loginDTO, "loginDTO");

        this.validator.validate(loginDTO, errors);

        if (errors.hasErrors()) {
            throw new BadCredentialsException("Invalid login data");
        }

        return super.authenticate(authentication);
    }
}
