package fitness.app.project.fitnessapp.dto;

import java.util.List;
import java.time.DayOfWeek;
import java.util.Set;

public record GetTemplateDTO(
        Integer id,
        String name,
        List<TemplateExerciseDTO> exercises,
        Set<DayOfWeek> scheduledDays
) {}
