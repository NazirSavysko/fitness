package fitness.app.project.fitnessapp.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ActiveWorkoutDTO(
        Integer sessionId,
        LocalDateTime startedAt,
        String templateName,
        List<SessionExerciseDTO> exercises
) {
}
