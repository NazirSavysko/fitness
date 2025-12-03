package fitness.app.project.fitnessapp.security;

import fitness.app.project.fitnessapp.model.DeactivatedToken;
import fitness.app.project.fitnessapp.repository.DeactivatedTokenRepository;
import fitness.app.project.fitnessapp.security.token.Token;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;

import java.util.Date;
import java.util.function.Function;

import static jakarta.servlet.http.HttpServletResponse.SC_NO_CONTENT;

@AllArgsConstructor
public final class TokenCookieAuthenticationConfigurer extends AbstractHttpConfigurer<TokenCookieAuthenticationConfigurer, HttpSecurity> {

    private static final String AUTH_TOKEN_COOKIE_NAME = "auth-token";

    private final Function<String, Token> tokenCookieStringDeserializer;
    private final DeactivatedTokenRepository deactivatedTokenRepository;
    private final AuthenticationProvider preAuthenticatedAuthenticationProvider;

    @Override
    public void init(@NonNull HttpSecurity builder) {
        builder.logout(logout ->
                logout
                        .logoutUrl("/api/v1/auth/logout")
                        .logoutSuccessUrl("/api/v1/auth/login")
                        .addLogoutHandler(new CookieClearingLogoutHandler(AUTH_TOKEN_COOKIE_NAME))
                        .addLogoutHandler((request, response, authentication) -> {
                            if (authentication != null &&
                                    authentication.getPrincipal() instanceof TokenUser user) {
                                final DeactivatedToken token = new DeactivatedToken(user.getToken().id(), Date.from(user.getToken().expiresAt()));
                                this.deactivatedTokenRepository.save(token);
                            }
                        })
        );
    }

    @Override
    public void configure(@NonNull HttpSecurity builder) {
        final AuthenticationFilter cookieAuthenticationFilter = new AuthenticationFilter(
                builder.getSharedObject(AuthenticationManager.class),
                new TokenCookieAuthenticationConverter(this.tokenCookieStringDeserializer)
        );
        cookieAuthenticationFilter
                .setSuccessHandler((request, response, authentication) -> {
                });
        cookieAuthenticationFilter.setFailureHandler((request, response, exception) -> {
            new Http403ForbiddenEntryPoint();
        });


        builder.addFilterAfter(cookieAuthenticationFilter, CsrfFilter.class)
                .authenticationProvider(preAuthenticatedAuthenticationProvider);

    }

}
