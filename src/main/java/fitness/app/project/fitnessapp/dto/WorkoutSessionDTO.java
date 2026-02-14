package fitness.app.project.fitnessapp.dto;

import java.time.LocalDateTime;
import java.util.List;

public record WorkoutSessionDTO(
 Integer sessionId,
 Integer templateId,
 LocalDateTime sessionDate,
 LocalDateTime endTime,
 List<WorkoutExerciseDTO> exercises) {}