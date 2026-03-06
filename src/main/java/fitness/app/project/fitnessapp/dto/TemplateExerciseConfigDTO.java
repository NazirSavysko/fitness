package fitness.app.project.fitnessapp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record TemplateExerciseConfigDTO(
        @NotNull(message = "Exercise is required")
        Integer exerciseId,
        @NotNull(message = "Normal sets are required")
        @PositiveOrZero(message = "Normal sets cannot be negative")
        Integer normalSets,
        @NotNull(message = "Failure sets are required")
        @PositiveOrZero(message = "Failure sets cannot be negative")
        Integer failureSets,
        @NotNull(message = "Rest time is required")
        @PositiveOrZero(message = "Rest time cannot be negative")
        Integer restSeconds
) {
}
