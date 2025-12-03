package fitness.app.project.fitnessapp.security;

import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import fitness.app.project.fitnessapp.repository.DeactivatedTokenRepository;
import fitness.app.project.fitnessapp.security.token.TokenCookieJwtStringSerializer;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final DeactivatedTokenRepository deactivatedTokenRepository;


    @Bean
    public TokenCookieJwtStringSerializer jwtStringSerializer(@Value("${jwt.cookie-token-key}") final String cookieTokenKey) throws Exception {
        return new TokenCookieJwtStringSerializer(new DirectEncrypter(OctetSequenceKey.parse(cookieTokenKey)));
    }

    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy(final TokenCookieJwtStringSerializer jwtStringSerializer) {
        final SessionAuthenticationStrategyImpl sessionAuthenticationStrategy = new SessionAuthenticationStrategyImpl();
        sessionAuthenticationStrategy.setTokenStringSerializer(jwtStringSerializer);

        return sessionAuthenticationStrategy;
    }

    @Bean
    public AuthenticationProvider preAuthenticatedAuthenticationProvider(){
        final PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(
                new TokenAuthenticationUserDetailsService(this.deactivatedTokenRepository)
        );

        return authenticationProvider;
    }


    @Bean
    public TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer(
            @Value("${jwt.cookie-token-key}") final String cookieTokenKey,
            AuthenticationProvider preAuthenticatedAuthenticationProvider) throws Exception {
        return new TokenCookieAuthenticationConfigurer(
                new TokenCookieJweStringDeserializer(
                        new DirectDecrypter(
                                OctetSequenceKey.parse(cookieTokenKey)
                        )
                ), this.deactivatedTokenRepository,preAuthenticatedAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity httpSecurity,
                                           final SessionAuthenticationStrategy sessionAuthenticationStrategy,
                                           final TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/api/v1/auth/**").permitAll()
                                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterAfter(new GetCsrfTokenFilter(), ExceptionTranslationFilter.class)
                .csrf(csrf ->
                        csrf.csrfTokenRepository(new CookieCsrfTokenRepository())
                                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                                .sessionAuthenticationStrategy(sessionAuthenticationStrategy)
                )
                .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
                .formLogin(form ->
                        form
                                .loginPage("/api/v1/auth/login")
                                .loginProcessingUrl("/api/v1/auth/log-in")
                                .usernameParameter("email")
                                .defaultSuccessUrl("/api/v1/dashboard", true)
                                .failureUrl("/api/v1/auth/login?error")
                                .permitAll()
                )
                .apply(tokenCookieAuthenticationConfigurer);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new DaoAuthenticationProvider(userDetailsService);
    }
}
