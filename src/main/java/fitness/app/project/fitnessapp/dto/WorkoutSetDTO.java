package fitness.app.project.fitnessapp.dto;

import java.math.BigDecimal;

public record WorkoutSetDTO(
        Long id,
        Integer setNumber,
        BigDecimal weight,
        Integer reps,
        boolean completed
) {}