package fitness.app.project.fitnessapp.security;

import fitness.app.project.fitnessapp.security.token.Token;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import java.time.Instant;
import java.util.Objects;
import java.util.function.Function;

import static java.time.temporal.ChronoUnit.SECONDS;

@Setter
public final class SessionAuthenticationStrategyImpl implements SessionAuthenticationStrategy {

    private static final String AUTH_TOKEN_COOKIE_NAME = "__HOST-auth-token";

    private Function<Authentication, Token> tokenCookieFactory ;
    private Function<Token, String> tokenStringSerializer= Objects::toString;

    @Override
    public void onAuthentication(final @NonNull Authentication authentication, final @NonNull HttpServletRequest request, final @NonNull HttpServletResponse response) throws SessionAuthenticationException {
        if(authentication instanceof UsernamePasswordAuthenticationToken){
            final Token token = this.tokenCookieFactory.apply(authentication);
            final String tokenString = this.tokenStringSerializer.apply(token);

            final Cookie cookie = new Cookie(AUTH_TOKEN_COOKIE_NAME, tokenString);
            cookie.setPath("/");
            cookie.setDomain(null);
            cookie.setHttpOnly(true);
            cookie.setMaxAge((int) SECONDS.between(Instant.now(), token.expiresAt()));

            response.addCookie(cookie);
        }

    }
}
