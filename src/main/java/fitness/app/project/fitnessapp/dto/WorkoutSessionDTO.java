package fitness.app.project.fitnessapp.dto;

import java.time.LocalDateTime;
import java.util.List;

public record WorkoutSessionDTO(
        Long id,
        String name,
        LocalDateTime startTime,
        List<WorkoutExerciseDTO> exercises
) {}