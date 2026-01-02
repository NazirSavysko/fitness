package fitness.app.project.fitnessapp.dto;

import java.util.List;

public record GetTemplateDTO(
        Integer id,
        String name,
        List<TemplateExerciseDTO> exercises
) {}