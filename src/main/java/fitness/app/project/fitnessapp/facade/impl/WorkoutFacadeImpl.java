package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.ActiveWorkoutDTO;
import fitness.app.project.fitnessapp.dto.AddSetDTO;
import fitness.app.project.fitnessapp.dto.ExerciseSetDTO;
import fitness.app.project.fitnessapp.dto.SessionExerciseDTO;
import fitness.app.project.fitnessapp.dto.UpdateExerciseSetDTO;
import fitness.app.project.fitnessapp.dto.WorkoutHistoryCardDTO;
import fitness.app.project.fitnessapp.facade.WorkoutFacade;
import fitness.app.project.fitnessapp.model.ExerciseSet;
import fitness.app.project.fitnessapp.model.SessionExercise;
import fitness.app.project.fitnessapp.model.WorkoutSession;
import fitness.app.project.fitnessapp.service.WorkoutSessionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public final class WorkoutFacadeImpl implements WorkoutFacade {

    private static final DateTimeFormatter DATE_TIME_DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    private final WorkoutSessionService workoutSessionService;

    @Override
    public Integer startWorkout(final Integer templateId, final String email) {
        return this.workoutSessionService.startWorkout(templateId, email);
    }

    @Override
    public Integer addSetToExercise(final AddSetDTO addSetDTO, final String email) {
        return this.workoutSessionService.addSetToExercise(addSetDTO, email);
    }

    @Override
    public Integer updateExerciseSet(final UpdateExerciseSetDTO updateExerciseSetDTO, final String email) {
        return this.workoutSessionService.updateExerciseSet(updateExerciseSetDTO, email);
    }

    @Override
    public void finishWorkout(final Integer sessionId, final String email) {
        this.workoutSessionService.finishWorkout(sessionId, email);
    }

    @Override
    public Page<WorkoutHistoryCardDTO> getHistory(final String email, final Long templateId, final String dateRange, final String sortBy, final Pageable pageable) {
        return this.workoutSessionService.getHistory(email, templateId, dateRange, sortBy, pageable)
                .map(workoutSession -> new WorkoutHistoryCardDTO(
                        workoutSession.getId(),
                        workoutSession.getSourceTemplate() == null ? "Free Workout" : workoutSession.getSourceTemplate().getName(),
                        workoutSession.getStartedAt().format(DATE_TIME_DISPLAY_FORMAT),
                        calculateDurationInMinutes(workoutSession),
                        calculateTotalSets(workoutSession),
                        calculateMuscleGroups(workoutSession)
                ));
    }

    @Override
    public ActiveWorkoutDTO getWorkoutDetails(final Integer sessionId, final String email) {
        final WorkoutSession workoutSession = this.workoutSessionService.getWorkoutDetails(sessionId, email);
        final List<SessionExercise> sessionExercises = workoutSession.getExercises() == null
                ? List.of()
                : workoutSession.getExercises();

        return new ActiveWorkoutDTO(
                workoutSession.getId(),
                workoutSession.getStartedAt(),
                workoutSession.getSourceTemplate() == null ? "Free Workout" : workoutSession.getSourceTemplate().getName(),
                sessionExercises.stream()
                        .map(sessionExercise -> new SessionExerciseDTO(
                                sessionExercise.getId(),
                                sessionExercise.getExercise().getName(),
                                sessionExercise.getExercise().getMuscleGroup(),
                                sessionExercise.getOrderIndex(),
                                sessionExercise.getSets() == null ? List.of() : sessionExercise.getSets().stream()
                                        .map(exerciseSet -> new ExerciseSetDTO(
                                                exerciseSet.getId(),
                                                exerciseSet.getSetNumber(),
                                                exerciseSet.getWeight(),
                                                exerciseSet.getReps(),
                                                exerciseSet.getRestSeconds(),
                                                exerciseSet.getSetType()
                                        )).toList()
                        )).toList()
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
}
