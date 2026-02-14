package fitness.app.project.fitnessapp.strategy.processing;

import fitness.app.project.fitnessapp.model.enums.VerificationType;

public interface VerificationProcessor {
    VerificationType getType();
    void process(String email);
}