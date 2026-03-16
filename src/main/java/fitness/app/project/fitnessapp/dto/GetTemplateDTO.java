package fitness.app.project.fitnessapp.dto;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

public record GetTemplateDTO(
        Integer id,
        String name,
        Set<DayOfWeek> scheduledDays,
        List<TemplateExerciseDTO> exercises
) {}
