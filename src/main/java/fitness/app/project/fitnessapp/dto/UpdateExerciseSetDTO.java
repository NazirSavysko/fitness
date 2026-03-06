package fitness.app.project.fitnessapp.dto;

import fitness.app.project.fitnessapp.annotations.ValidReps;
import fitness.app.project.fitnessapp.annotations.ValidWeight;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateExerciseSetDTO(
        @NotNull(message = "Set id is required")
        Integer setId,
        @NotNull(message = "Weight is required")
        @ValidWeight
        BigDecimal weight,
        @NotNull(message = "Reps is required")
        @ValidReps
        Integer reps
) {
}
