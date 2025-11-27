package fitness.app.project.fitnessapp.security;

import fitness.app.project.fitnessapp.security.token.Token;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.csrf.CsrfFilter;

import java.util.Date;
import java.util.function.Function;


public final class TokenCookieAuthenticationConfigurer
        extends AbstractHttpConfigurer<TokenCookieAuthenticationConfigurer, HttpSecurity> {

    private Function<String, Token> tokenCookieStringDeserializer;

    @Override
    public void init(@NonNull HttpSecurity builder) {
        builder.logout(logout -> logout.addLogoutHandler(new CookieClearingLogoutHandler("__Host-auth-token"))
                        .addLogoutHandler((request, response, authentication) -> {
                            if (authentication != null &&
                                    authentication.getPrincipal() instanceof TokenUser user) {
//                                this.jdbcTemplate.update("insert into t_deactivated_token (id, c_keep_until) values (?, ?)",
//                                        user.getToken().id(), Date.from(user.getToken().expiresAt()));

                                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
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

        builder.addFilterAfter(cookieAuthenticationFilter, CsrfFilter.class);

    }

}
