package fitness.app.project.fitnessapp.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record UpdateTemplateDTO(
        Integer id,
        String name,
        @Valid
        @NotEmpty(message = "You must add at least one exercise")
        List<TemplateExerciseDTO> exercises
) {}