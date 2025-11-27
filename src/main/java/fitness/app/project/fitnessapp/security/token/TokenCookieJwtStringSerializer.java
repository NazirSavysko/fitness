package fitness.app.project.fitnessapp.security.token;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.Setter;
import lombok.SneakyThrows;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

import static com.nimbusds.jose.EncryptionMethod.A128GCM;
import static com.nimbusds.jose.JWEAlgorithm.DIR;
import static java.util.Date.from;

public final class TokenCookieJwtStringSerializer implements Function<Token, String> {

    private final JWEEncrypter jweEncrypter;
    @Setter
    private JWEAlgorithm jweAlgorithm = DIR;
    @Setter
    private EncryptionMethod encryptionMethod = A128GCM;

    public TokenCookieJwtStringSerializer(JWEEncrypter jweEncrypter) {
        this.jweEncrypter = jweEncrypter;
    }

    public TokenCookieJwtStringSerializer(JWEEncrypter jweEncrypter, JWEAlgorithm jweAlgorithm, EncryptionMethod encryptionMethod) {
        this.jweEncrypter = jweEncrypter;
        this.jweAlgorithm = jweAlgorithm;
        this.encryptionMethod = encryptionMethod;
    }

    @SneakyThrows(JOSEException.class)
    @Override
    public String apply(final @NonNull Token token) {
      final JWEHeader jwsHeader = new JWEHeader.Builder(jweAlgorithm, encryptionMethod)
              .keyID(token.id().toString())
              .build();

      final JWTClaimsSet  claimsSet = new JWTClaimsSet.Builder()
                .jwtID(token.id().toString())
                .subject(token.subject())
                .issueTime(from(token.createdAt()))
                .expirationTime(from(token.expiresAt()))
                .claim("authorities", token.authorities())
                .build();

      final EncryptedJWT encryptedJWT = new EncryptedJWT(jwsHeader, claimsSet);
            encryptedJWT.encrypt(this.jweEncrypter);

            return encryptedJWT.serialize();
    }
}
