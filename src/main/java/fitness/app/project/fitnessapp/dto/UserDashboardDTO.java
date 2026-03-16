package fitness.app.project.fitnessapp.dto;

import java.time.LocalDate;
import java.util.List;

public record UserDashboardDTO(
        LocalDate today,
        List<LocalDate> weekDates,
        List<GetDashboardTemplateDTO> templates,
        List<DashboardWorkoutDTO> workouts
) {}
