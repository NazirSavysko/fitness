package fitness.app.project.fitnessapp.security.token;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.function.Function;
import java.util.stream.Stream;

@AllArgsConstructor
public final class TokenCookieAuthenticationConverter implements AuthenticationConverter {

    private final Function<String, Token> tokenCookieStringDeserializer;

    @Override
    public @Nullable Authentication convert(final @NonNull HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Stream.of(request.getCookies())
                .filter(cookie -> cookie.getName().equals("__HOST-auth-token"))
                .findFirst()
                .map(cookie -> {
                    final Token token = this.tokenCookieStringDeserializer.apply(cookie.getValue());
                    return new PreAuthenticatedAuthenticationToken(token, cookie.getValue());
                })
                .orElse(null);
    }
}
