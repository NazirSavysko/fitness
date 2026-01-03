package fitness.app.project.fitnessapp.security.config;

import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import fitness.app.project.fitnessapp.repository.DeactivatedTokenRepository;
import fitness.app.project.fitnessapp.security.filter.GetCsrfTokenFilter;
import fitness.app.project.fitnessapp.security.provider.DaoAuthenticationProviderWithValidation;
import fitness.app.project.fitnessapp.security.service.TokenAuthenticationUserDetailsService;
import fitness.app.project.fitnessapp.security.strategy.SessionAuthenticationStrategyImpl;
import fitness.app.project.fitnessapp.security.token.DefaultTokenCookieFactory;
import fitness.app.project.fitnessapp.security.token.TokenCookieJweStringDeserializer;
import fitness.app.project.fitnessapp.security.token.TokenCookieJwtStringSerializer;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
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
import org.springframework.validation.Validator;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final DeactivatedTokenRepository deactivatedTokenRepository;


    @Bean
    public TokenCookieJwtStringSerializer jwtStringSerializer(final @Value("${jwt.cookie-token-key}") String cookieTokenKey) throws Exception {
        return new TokenCookieJwtStringSerializer(new DirectEncrypter(OctetSequenceKey.parse(cookieTokenKey)));
    }

    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy(final TokenCookieJwtStringSerializer jwtStringSerializer) {
        final SessionAuthenticationStrategyImpl sessionAuthenticationStrategy = new SessionAuthenticationStrategyImpl();
        sessionAuthenticationStrategy.setTokenStringSerializer(jwtStringSerializer);
        sessionAuthenticationStrategy.setTokenCookieFactory(new DefaultTokenCookieFactory());

        return sessionAuthenticationStrategy;
    }

    @Bean("tokenAuthProvider")
    public AuthenticationProvider preAuthenticatedAuthenticationProvider() {
        final PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(
                new TokenAuthenticationUserDetailsService(this.deactivatedTokenRepository)
        );

        return authenticationProvider;
    }


    @Bean
    public TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer(
            final @Value("${jwt.cookie-token-key}") String cookieTokenKey,
            final @Qualifier("tokenAuthProvider") AuthenticationProvider preAuthenticatedAuthenticationProvider) throws Exception {
        return new TokenCookieAuthenticationConfigurer(
                new TokenCookieJweStringDeserializer(
                        new DirectDecrypter(
                                OctetSequenceKey.parse(cookieTokenKey)
                        )
                ), this.deactivatedTokenRepository, preAuthenticatedAuthenticationProvider);
    }

    @Bean
    public AuthenticationManager authenticationManager(
            final @Qualifier("dbAuthProvider") AuthenticationProvider dbAuthProvider,
            final @Qualifier("tokenAuthProvider") AuthenticationProvider tokenAuthProvider) {
        return new ProviderManager(List.of(dbAuthProvider, tokenAuthProvider));
    }

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity httpSecurity,
                                           final SessionAuthenticationStrategy sessionAuthenticationStrategy,
                                           final TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer,
                                           final @Value("${my.super.secret.key}") String secretKey,
                                           final AuthenticationManager authenticationManager) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/auth/**").permitAll()
                                .requestMatchers("/verification/**").permitAll()
                                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                                .anyRequest().authenticated()
                )
                .rememberMe(remember -> remember
                        .key(secretKey)
                        .tokenValiditySeconds(60 * 60 * 24 * 7)
                        .userDetailsService(userDetailsService)
                        .rememberMeParameter("remember-me")
                )
                .authenticationManager(authenticationManager)
                .addFilterAfter(new GetCsrfTokenFilter(), ExceptionTranslationFilter.class)
                .csrf(csrf ->
                        csrf.csrfTokenRepository(new CookieCsrfTokenRepository())
                                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                                .sessionAuthenticationStrategy(sessionAuthenticationStrategy)
                )
                .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
                .formLogin(form ->
                        form
                                .loginPage("/auth/login")
                                .loginProcessingUrl("/auth/log-in")
                                .usernameParameter("email")
                                .defaultSuccessUrl("/dashboard", true)
                                .failureUrl("/auth/login?error")
                                .permitAll()
                )
                .apply(tokenCookieAuthenticationConfigurer);

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean("dbAuthProvider")
    public AuthenticationProvider authenticationProvider(final Validator validator) {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProviderWithValidation(userDetailsService, validator);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
