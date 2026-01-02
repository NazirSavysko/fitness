package fitness.app.project.fitnessapp.dto;

import java.util.List;

public record UpdateTemplateDTO(
        Integer id,
        String name,
        List<TemplateExerciseDTO> exercises
) {}