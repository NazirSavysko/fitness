package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.DashboardWorkoutDTO;
import fitness.app.project.fitnessapp.dto.UserDashboardDTO;
import fitness.app.project.fitnessapp.facade.DashboardFacade;
import fitness.app.project.fitnessapp.mapper.GetTemplateForDashboardMapper;
import fitness.app.project.fitnessapp.model.SessionExercise;
import fitness.app.project.fitnessapp.model.TemplateExercise;
import fitness.app.project.fitnessapp.model.WorkoutSession;
import fitness.app.project.fitnessapp.service.WorkoutSessionService;
import fitness.app.project.fitnessapp.service.WorkoutTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static fitness.app.project.fitnessapp.utils.MapperUtils.mapList;

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
        final LocalDate weekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        final List<LocalDate> weekDates = IntStream.range(0, 7)
                .mapToObj(weekStart::plusDays)
                .toList();
        final LocalDateTime weekStartAt = weekStart.atStartOfDay();
        final LocalDateTime weekEndAt = weekStart.plusDays(7).atStartOfDay().minusNanos(1);

        final List<DashboardWorkoutDTO> workouts = this.workoutSessionService
                .getSessionsForDateRange(userEmail, weekStartAt, weekEndAt).stream()
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

    private static long calculateDurationInMinutes(final WorkoutSession workoutSession) {
        if (workoutSession.getEndedAt() == null) {
            return 0;
        }
        return Duration.between(workoutSession.getStartedAt(), workoutSession.getEndedAt()).toMinutes();
    }

    private static int calculateTotalSets(final WorkoutSession workoutSession) {
        if (workoutSession.getExercises() == null) {
            return 0;
        }
        return workoutSession.getExercises().stream()
                .mapToInt(sessionExercise -> sessionExercise.getSets() == null ? 0 : sessionExercise.getSets().size())
                .sum();
    }

    private static String calculateMuscleGroups(final WorkoutSession workoutSession) {
        if (workoutSession.getExercises() == null) {
            return "";
        }
        return String.join(", ", workoutSession.getExercises().stream()
                .map(SessionExercise::getExercise)
                .filter(exerciseDefinition -> exerciseDefinition != null && exerciseDefinition.getMuscleGroup() != null)
                .map(exerciseDefinition -> exerciseDefinition.getMuscleGroup().trim())
                .filter(muscleGroup -> !muscleGroup.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    private static int calculateCompletionPercentage(final WorkoutSession workoutSession) {
        if (workoutSession.getSourceTemplate() == null || workoutSession.getSourceTemplate().getExercises() == null) {
            return 0;
        }
        final int expectedSets = workoutSession.getSourceTemplate().getExercises().stream()
                .mapToInt(DashboardFacadeImpl::countExpectedSets)
                .sum();
        if (expectedSets <= 0 || workoutSession.getExercises() == null) {
            return 0;
        }
        final long completedSets = workoutSession.getExercises().stream()
                .map(SessionExercise::getSets)
                .filter(sets -> sets != null)
                .flatMap(List::stream)
                .filter(set -> set.getWeight() != null && set.getReps() != null)
                .count();
        return (int) ((completedSets * 100) / expectedSets);
    }

    private static int countExpectedSets(final TemplateExercise templateExercise) {
        final int normalSets = templateExercise.getNormalSets() == null ? 0 : templateExercise.getNormalSets();
        final int failureSets = templateExercise.getFailureSets() == null ? 0 : templateExercise.getFailureSets();
        return normalSets + failureSets;
    }
}
