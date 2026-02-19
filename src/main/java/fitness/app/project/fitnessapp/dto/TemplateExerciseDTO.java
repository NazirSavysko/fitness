package fitness.app.project.fitnessapp.dto;

public record TemplateExerciseDTO(
        Integer exerciseId,
        String exerciseName,
        String muscleGroup,
        Integer orderIndex
) {}
