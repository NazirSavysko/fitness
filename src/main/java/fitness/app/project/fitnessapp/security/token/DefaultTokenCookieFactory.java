package fitness.app.project.fitnessapp.security.token;

import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Function;

import static java.time.Duration.ofDays;

@Setter
public final class DefaultTokenCookieFactory implements Function<Authentication, Token> {

    private Duration tokenTtl = ofDays(1);

    @Override
    public Token apply(Authentication authentication) {
        var now = Instant.now();
        return new Token(UUID.randomUUID(), authentication.getName(),
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList(),
                now, now.plus(this.tokenTtl));
    }

}