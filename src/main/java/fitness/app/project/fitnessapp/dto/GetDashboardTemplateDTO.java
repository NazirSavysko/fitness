package fitness.app.project.fitnessapp.dto;

import java.time.DayOfWeek;
import java.util.Set;

public record GetDashboardTemplateDTO(
    Integer id,
    String name,
    Set<DayOfWeek> scheduledDays
) { }
