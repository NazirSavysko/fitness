package fitness.app.project.fitnessapp.strategy.verification;

import fitness.app.project.fitnessapp.model.VerificationType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Component
public final class VerificationRedirectService {
    private static final String STRATEGY_NOT_FOUND_MESSAGE = "No strategy found for verification type: %s";

    private final Map<VerificationType, VerificationRedirectStrategy> strategyMap;

    public VerificationRedirectService(final List<VerificationRedirectStrategy> strategies) {
        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(VerificationRedirectStrategy::getType, Function.identity()));
    }

    public String getRedirectUrl(final VerificationType type,final String email,final String code) {
        VerificationRedirectStrategy strategy = this.strategyMap.get(type);

        if (strategy == null) {
            throw new IllegalArgumentException(format(STRATEGY_NOT_FOUND_MESSAGE, type));
        }

        return strategy.getRedirectUrl(email, code);
    }
}