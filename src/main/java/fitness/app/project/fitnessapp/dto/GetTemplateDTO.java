package fitness.app.project.fitnessapp.dto;

import java.util.List;
import java.time.DayOfWeek;

public record GetTemplateDTO(
        Integer id,
        String name,
        List<TemplateExerciseDTO> exercises,
        DayOfWeek scheduledDay
) {}
