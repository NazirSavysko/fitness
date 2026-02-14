package fitness.app.project.fitnessapp.dto;

import java.math.BigDecimal;

public record WorkoutSetDTO(
 Integer logId,
 Integer setNumber,
 Double weight,
 Integer reps,
 Integer restSeconds) {}