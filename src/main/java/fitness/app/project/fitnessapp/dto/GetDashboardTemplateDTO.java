package fitness.app.project.fitnessapp.dto;

import java.time.DayOfWeek;

public record GetDashboardTemplateDTO(
    Integer id,
    String name,
    DayOfWeek scheduledDay
) { }
