package fitness.app.project.fitnessapp.service.impl;

import fitness.app.project.fitnessapp.dto.AddSetDTO;
import fitness.app.project.fitnessapp.dto.BulkSetUpdateDTO;
import fitness.app.project.fitnessapp.model.ExerciseDefinition;
import fitness.app.project.fitnessapp.model.ExerciseSet;
import fitness.app.project.fitnessapp.model.SessionExercise;
import fitness.app.project.fitnessapp.model.TemplateExercise;
import fitness.app.project.fitnessapp.model.User;
import fitness.app.project.fitnessapp.model.WorkoutSession;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import fitness.app.project.fitnessapp.model.enums.SetType;
import fitness.app.project.fitnessapp.repository.ExerciseSetRepository;
import fitness.app.project.fitnessapp.repository.SessionExerciseRepository;
import fitness.app.project.fitnessapp.repository.WorkoutSessionRepository;
import fitness.app.project.fitnessapp.service.UserService;
import fitness.app.project.fitnessapp.service.WorkoutTemplateService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WorkoutSessionServiceImplTest {

    private final WorkoutTemplateService workoutTemplateService = mock(WorkoutTemplateService.class);
    private final UserService userService = mock(UserService.class);
    private final WorkoutSessionRepository workoutSessionRepository = mock(WorkoutSessionRepository.class);
    private final SessionExerciseRepository sessionExerciseRepository = mock(SessionExerciseRepository.class);
    private final ExerciseSetRepository exerciseSetRepository = mock(ExerciseSetRepository.class);

    private final WorkoutSessionServiceImpl workoutSessionService = new WorkoutSessionServiceImpl(
            workoutTemplateService,
            userService,
            workoutSessionRepository,
            sessionExerciseRepository,
            exerciseSetRepository
    );

    @Test
    void startWorkoutCopiesTemplateExercisesWithOrderAndPreGeneratesSets() {
        final WorkoutTemplate template = new WorkoutTemplate();
        template.setExercises(List.of(
                templateExercise(4, 0, 2, 1),
                templateExercise(7, 1, 1, 0)
        ));
        final User user = new User();
        final WorkoutSession saved = new WorkoutSession();
        saved.setId(42);

        when(workoutTemplateService.getWorkoutTemplateById(10, "user@mail.com")).thenReturn(template);
        when(userService.getUserByEmail("user@mail.com")).thenReturn(user);
        when(workoutSessionRepository.save(any(WorkoutSession.class))).thenReturn(saved);

        final Integer sessionId = workoutSessionService.startWorkout(10, "user@mail.com");

        final ArgumentCaptor<WorkoutSession> captor = ArgumentCaptor.forClass(WorkoutSession.class);
        verify(workoutSessionRepository).save(captor.capture());
        final WorkoutSession created = captor.getValue();

        assertEquals(42, sessionId);
        assertSame(user, created.getUser());
        assertSame(template, created.getSourceTemplate());
        assertEquals(2, created.getExercises().size());
        assertEquals(0, created.getExercises().get(0).getOrderIndex());
        assertEquals(4, created.getExercises().get(0).getExercise().getId());
        assertEquals(3, created.getExercises().get(0).getSets().size());
        assertEquals(SetType.NORMAL, created.getExercises().get(0).getSets().get(0).getSetType());
        assertEquals(SetType.NORMAL, created.getExercises().get(0).getSets().get(1).getSetType());
        assertEquals(SetType.FAILURE, created.getExercises().get(0).getSets().get(2).getSetType());
        assertEquals(1, created.getExercises().get(1).getOrderIndex());
        assertEquals(7, created.getExercises().get(1).getExercise().getId());
        assertEquals(1, created.getExercises().get(1).getSets().size());
    }

    @Test
    void addSetToExerciseUsesNextSetNumber() {
        final SessionExercise sessionExercise = new SessionExercise();
        final WorkoutSession workoutSession = new WorkoutSession();
        workoutSession.setId(5);
        sessionExercise.setSession(workoutSession);
        sessionExercise.setId(9);
        final ExerciseSet existingSet = new ExerciseSet();
        existingSet.setSetNumber(3);

        when(sessionExerciseRepository.findByIdAndSession_User_Email(9, "user@mail.com")).thenReturn(Optional.of(sessionExercise));
        when(exerciseSetRepository.findTopBySessionExercise_IdOrderBySetNumberDesc(9)).thenReturn(Optional.of(existingSet));

        final Integer sessionId = workoutSessionService.addSetToExercise(
                new AddSetDTO(new BigDecimal("100.5"), 8, 90, SetType.NORMAL, 9),
                "user@mail.com"
        );

        final ArgumentCaptor<ExerciseSet> captor = ArgumentCaptor.forClass(ExerciseSet.class);
        verify(exerciseSetRepository).save(captor.capture());
        final ExerciseSet savedSet = captor.getValue();

        assertEquals(5, sessionId);
        assertEquals(4, savedSet.getSetNumber());
        assertEquals(new BigDecimal("100.5"), savedSet.getWeight());
        assertEquals(8, savedSet.getReps());
        assertEquals(90, savedSet.getRestSeconds());
        assertEquals(SetType.NORMAL, savedSet.getSetType());
        assertSame(sessionExercise, savedSet.getSessionExercise());
    }

    @Test
    void finishWorkoutSetsEndedAtAndSavesSession() {
        final WorkoutSession session = new WorkoutSession();
        session.setId(13);
        session.setEndedAt(null);
        when(workoutSessionRepository.findByIdAndUser_Email(13, "user@mail.com")).thenReturn(Optional.of(session));

        workoutSessionService.finishWorkout(13, "user@mail.com");

        assertNotNull(session.getEndedAt());
        verify(workoutSessionRepository).save(session);
    }

    @Test
    void updateExerciseSetUpdatesWeightAndReps() {
        final WorkoutSession workoutSession = new WorkoutSession();
        workoutSession.setId(22);
        final SessionExercise sessionExercise = new SessionExercise();
        sessionExercise.setSession(workoutSession);
        final ExerciseSet exerciseSet = new ExerciseSet();
        exerciseSet.setId(15);
        exerciseSet.setSessionExercise(sessionExercise);

        when(exerciseSetRepository.findByIdAndSessionExercise_Session_User_Email(15, "user@mail.com"))
                .thenReturn(Optional.of(exerciseSet));

        final Integer sessionId = workoutSessionService.updateExerciseSet(
                new fitness.app.project.fitnessapp.dto.UpdateExerciseSetDTO(15, new BigDecimal("82.5"), 7),
                "user@mail.com"
        );

        assertEquals(22, sessionId);
        assertEquals(new BigDecimal("82.5"), exerciseSet.getWeight());
        assertEquals(7, exerciseSet.getReps());
        verify(exerciseSetRepository).save(exerciseSet);
    }

    @Test
    void bulkUpdateSetsUpdatesWeightRepsAndRestSeconds() {
        final ExerciseSet firstSet = new ExerciseSet();
        firstSet.setId(11);
        final ExerciseSet secondSet = new ExerciseSet();
        secondSet.setId(12);

        when(exerciseSetRepository.findByIdAndSessionExercise_Session_User_Email(11, "user@mail.com"))
                .thenReturn(Optional.of(firstSet));
        when(exerciseSetRepository.findByIdAndSessionExercise_Session_User_Email(12, "user@mail.com"))
                .thenReturn(Optional.of(secondSet));

        workoutSessionService.bulkUpdateSets(
                List.of(
                        new BulkSetUpdateDTO(11, new BigDecimal("90.5"), 6, 120),
                        new BulkSetUpdateDTO(12, new BigDecimal("82.5"), 10, 90)
                ),
                "user@mail.com"
        );

        assertEquals(new BigDecimal("90.5"), firstSet.getWeight());
        assertEquals(6, firstSet.getReps());
        assertEquals(120, firstSet.getRestSeconds());
        assertEquals(new BigDecimal("82.5"), secondSet.getWeight());
        assertEquals(10, secondSet.getReps());
        assertEquals(90, secondSet.getRestSeconds());
        verify(exerciseSetRepository, times(2)).save(any(ExerciseSet.class));
    }

    private static TemplateExercise templateExercise(final int exerciseId,
                                                     final int orderIndex,
                                                     final int normalSets,
                                                     final int failureSets) {
        final ExerciseDefinition exerciseDefinition = new ExerciseDefinition();
        exerciseDefinition.setId(exerciseId);
        final TemplateExercise templateExercise = new TemplateExercise();
        templateExercise.setExercise(exerciseDefinition);
        templateExercise.setOrderIndex(orderIndex);
        templateExercise.setNormalSets(normalSets);
        templateExercise.setFailureSets(failureSets);
        return templateExercise;
    }
}
