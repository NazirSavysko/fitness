package fitness.app.project.fitnessapp.dto;

import fitness.app.project.fitnessapp.model.enums.SetType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record AddSetDTO(
        @NotNull(message = "Weight is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Weight cannot be negative")
        BigDecimal weight,
        @NotNull(message = "Reps is required")
        @PositiveOrZero(message = "Reps cannot be negative")
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
