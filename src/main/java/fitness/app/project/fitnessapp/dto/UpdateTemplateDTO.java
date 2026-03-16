package fitness.app.project.fitnessapp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.List;

public record UpdateTemplateDTO(
        Integer id,
        String name,
        @Valid
        @NotEmpty(message = "You must add at least one exercise")
        List<@NotNull TemplateExerciseConfigDTO> exercises,
        @NotNull(message = "Please select a day of the week")
        DayOfWeek scheduledDay
) {}
