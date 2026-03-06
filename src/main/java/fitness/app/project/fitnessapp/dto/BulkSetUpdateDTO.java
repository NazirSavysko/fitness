package fitness.app.project.fitnessapp.dto;

import fitness.app.project.fitnessapp.annotations.ValidReps;
import fitness.app.project.fitnessapp.annotations.ValidWeight;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record BulkSetUpdateDTO(
        @NotNull(message = "Set id is required")
        Integer setId,
        @ValidWeight
        BigDecimal weight,
        @ValidReps
        Integer reps,
        @PositiveOrZero(message = "Rest seconds cannot be negative")
        Integer restSeconds
) {
}
