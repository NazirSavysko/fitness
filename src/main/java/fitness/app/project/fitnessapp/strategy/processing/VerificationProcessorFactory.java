package fitness.app.project.fitnessapp.strategy.processing;

import fitness.app.project.fitnessapp.model.VerificationType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Component
public final class VerificationProcessorFactory {
    private static final String PROCESSOR_NOT_FOUND_MESSAGE = "No processor found for type: %s";

    private final Map<VerificationType, VerificationProcessor> processorMap;

    public VerificationProcessorFactory(final List<VerificationProcessor> processors) {
        this.processorMap = processors.stream()
                .collect(Collectors.toMap(VerificationProcessor::getType, Function.identity()));
    }

    public void process(final VerificationType type,final  String email) {
        VerificationProcessor processor = this.processorMap.get(type);
        if (processor == null) {
            throw new IllegalArgumentException(format(PROCESSOR_NOT_FOUND_MESSAGE, type));
        }

        processor.process(email);
    }
}