package fitness.app.project.fitnessapp.dto;

import jakarta.validation.constraints.NotNull;

public record TemplateExerciseDTO(
        @NotNull(message = "Please select an exercise")
        Long id,
        String exerciseName
) {}