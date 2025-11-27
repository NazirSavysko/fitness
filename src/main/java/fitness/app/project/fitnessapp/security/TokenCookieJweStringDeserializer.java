package fitness.app.project.fitnessapp.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEDecrypter;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import fitness.app.project.fitnessapp.security.token.Token;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NonNull;

import java.text.ParseException;
import java.util.function.Function;

import static java.util.UUID.fromString;

@AllArgsConstructor
public final class TokenCookieJweStringDeserializer implements Function<String, Token> {

    private final JWEDecrypter jweDecrypter;

    @Contract("_ -> new")
    @SneakyThrows({ParseException.class, JOSEException.class})
    @Override
    public @NonNull Token apply(String string) {
        final EncryptedJWT encryptedJWT = EncryptedJWT.parse(string);
        encryptedJWT.decrypt(this.jweDecrypter);
        final JWTClaimsSet claimsSet = encryptedJWT.getJWTClaimsSet();

        return new Token(fromString(claimsSet.getJWTID()), claimsSet.getSubject(),
                claimsSet.getStringListClaim("authorities"),
                claimsSet.getIssueTime().toInstant(),
                claimsSet.getExpirationTime().toInstant());
    }
}
