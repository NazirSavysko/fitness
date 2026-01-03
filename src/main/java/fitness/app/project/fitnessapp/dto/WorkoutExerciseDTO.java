package fitness.app.project.fitnessapp.dto;

import java.util.List;

public record WorkoutExerciseDTO(
        Long exerciseId,
        String exerciseName,
        List<WorkoutSetDTO> sets
) {}