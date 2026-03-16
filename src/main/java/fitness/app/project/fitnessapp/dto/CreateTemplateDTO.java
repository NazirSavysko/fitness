package fitness.app.project.fitnessapp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.DayOfWeek;
import java.util.List;

public record CreateTemplateDTO(
        @NotBlank(message = "Template name cannot be empty")
        @Size(min = 3, max = 50, message = "Name must be between {min} and {max} characters")
        String name,
        @Valid
        @NotEmpty(message = "You must add at least one exercise")
        List<@NotNull TemplateExerciseConfigDTO> exercises,
        @NotNull(message = "Please select a day of the week")
        DayOfWeek scheduledDay
) {}
