package fitness.app.project.fitnessapp.dto;

import fitness.app.project.fitnessapp.model.enums.SetType;

import java.math.BigDecimal;

public record ExerciseSetDTO(
        Integer id,
        Integer setNumber,
        BigDecimal weight,
        Integer reps,
        Integer restSeconds,
        SetType setType
) {
}
