package fitness.app.project.fitnessapp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public record UpdateTemplateDTO(
        Integer id,
        String name,
        Set<DayOfWeek> scheduledDays,
        @Valid
        @NotEmpty(message = "You must add at least one exercise")
        List<@NotNull TemplateExerciseConfigDTO> exercises
) {}
