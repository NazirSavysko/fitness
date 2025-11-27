package fitness.app.project.fitnessapp.security.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Function;

import static java.time.Duration.ofDays;
import static java.time.Instant.now;

public class TokenCookieFactoryImpl implements Function<Authentication, Token> {

    private static final Duration TOKEN_EXPIRATION_DURATION = ofDays(1);

    @Override
    public Token apply(final Authentication authentication) {
        return new Token(UUID.randomUUID(),authentication.getName(),authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                now(), now().plus(TOKEN_EXPIRATION_DURATION));
    }
}
