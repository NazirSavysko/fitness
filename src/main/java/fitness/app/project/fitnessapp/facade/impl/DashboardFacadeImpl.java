package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.DashboardWorkoutDTO;
import fitness.app.project.fitnessapp.dto.UserDashboardDTO;
import fitness.app.project.fitnessapp.facade.DashboardFacade;
import fitness.app.project.fitnessapp.mapper.GetTemplateForDashboardMapper;
import fitness.app.project.fitnessapp.model.WorkoutSession;
import fitness.app.project.fitnessapp.service.WorkoutSessionService;
import fitness.app.project.fitnessapp.service.WorkoutTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.IntStream;

import static fitness.app.project.fitnessapp.utils.MapperUtils.mapList;
import static fitness.app.project.fitnessapp.utils.WorkoutSessionMetrics.calculateCompletionPercentage;
import static fitness.app.project.fitnessapp.utils.WorkoutSessionMetrics.calculateDurationInMinutes;
import static fitness.app.project.fitnessapp.utils.WorkoutSessionMetrics.calculateMuscleGroups;
import static fitness.app.project.fitnessapp.utils.WorkoutSessionMetrics.calculateTotalSets;

@Component
@AllArgsConstructor
public final class DashboardFacadeImpl implements DashboardFacade {
    private static final DateTimeFormatter DATE_TIME_DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private final WorkoutTemplateService workoutTemplateService;
    private final WorkoutSessionService workoutSessionService;
    private final GetTemplateForDashboardMapper getTemplateForDashboardMapper;

    @Override
    public UserDashboardDTO loadUserDashboardData(final String userEmail) {
        final LocalDate today = LocalDate.now();
        final LocalDate weekStartDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        final List<LocalDate> weekDates = IntStream.range(0, 7)
                .mapToObj(weekStartDate::plusDays)
                .toList();
        final LocalDateTime weekStartDateTime = weekStartDate.atStartOfDay();
        final LocalDateTime weekEndDateTime = weekStartDate.plusDays(7).atStartOfDay().minusNanos(1);

        final List<DashboardWorkoutDTO> workouts = this.workoutSessionService
                .getSessionsForDateRange(userEmail, weekStartDateTime, weekEndDateTime).stream()
                .map(DashboardFacadeImpl::toDashboardWorkout)
                .toList();

        return new UserDashboardDTO(
                today,
                weekDates,
                mapList(this.workoutTemplateService.getTemplatesByUserEmail(userEmail), this.getTemplateForDashboardMapper),
                workouts
        );
    }

    private static DashboardWorkoutDTO toDashboardWorkout(final WorkoutSession workoutSession) {
        return new DashboardWorkoutDTO(
                workoutSession.getId(),
                workoutSession.getSourceTemplate() == null ? "Free Workout" : workoutSession.getSourceTemplate().getName(),
                workoutSession.getStartedAt().toLocalDate(),
                workoutSession.getStartedAt().format(DATE_TIME_DISPLAY_FORMAT),
                calculateDurationInMinutes(workoutSession),
                calculateTotalSets(workoutSession),
                calculateMuscleGroups(workoutSession),
                calculateCompletionPercentage(workoutSession),
                workoutSession.getEndedAt() == null
        );
    }
}
