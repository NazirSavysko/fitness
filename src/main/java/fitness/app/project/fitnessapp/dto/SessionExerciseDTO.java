package fitness.app.project.fitnessapp.dto;

import java.util.List;

public record SessionExerciseDTO(
        Integer id,
        String exerciseName,
        String muscleGroup,
        Integer orderIndex,
        List<ExerciseSetDTO> sets
) {
}
