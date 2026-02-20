package fitness.app.project.fitnessapp.dto;

import fitness.app.project.fitnessapp.annotations.ValidReps;
import fitness.app.project.fitnessapp.annotations.ValidWeight;
import fitness.app.project.fitnessapp.model.enums.SetType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record AddSetDTO(
        @NotNull(message = "Weight is required")
        @ValidWeight
        BigDecimal weight,
        @NotNull(message = "Reps is required")
        @ValidReps
        Integer reps,
        @NotNull(message = "Rest is required")
        @PositiveOrZero(message = "Rest cannot be negative")
        Integer restSeconds,
        @NotNull(message = "Set type is required")
        SetType setType,
        @NotNull(message = "Session exercise is required")
        Integer sessionExerciseId
) {
}
