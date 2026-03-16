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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
        when(workoutSessionRepository.saveAndFlush(any(WorkoutSession.class))).thenReturn(saved);
        when(sessionExerciseRepository.saveAll(anyList())).thenAnswer(invocation -> {
            final List<SessionExercise> exercises = invocation.getArgument(0);
            for (int index = 0; index < exercises.size(); index++) {
                exercises.get(index).setId(index + 100);
            }
            return exercises;
        });
        when(exerciseSetRepository.saveAll(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

        final Integer sessionId = workoutSessionService.startWorkout(10, "user@mail.com");

        final ArgumentCaptor<WorkoutSession> sessionCaptor = ArgumentCaptor.forClass(WorkoutSession.class);
        verify(workoutSessionRepository).saveAndFlush(sessionCaptor.capture());
        final WorkoutSession created = sessionCaptor.getValue();

        final ArgumentCaptor<List<SessionExercise>> exercisesCaptor = ArgumentCaptor.forClass(List.class);
        verify(sessionExerciseRepository).saveAll(exercisesCaptor.capture());
        final List<SessionExercise> savedExercises = exercisesCaptor.getValue();

        final ArgumentCaptor<List<ExerciseSet>> setsCaptor = ArgumentCaptor.forClass(List.class);
        verify(exerciseSetRepository).saveAll(setsCaptor.capture());
        final List<ExerciseSet> savedSets = setsCaptor.getValue();

        assertEquals(42, sessionId);
        assertSame(user, created.getUser());
        assertSame(template, created.getSourceTemplate());
        assertEquals(2, savedExercises.size());
        assertEquals(0, savedExercises.get(0).getOrderIndex());
        assertEquals(4, savedExercises.get(0).getExercise().getId());
        assertEquals(3, savedExercises.get(0).getSets().size());
        assertEquals(SetType.NORMAL, savedExercises.get(0).getSets().get(0).getSetType());
        assertEquals(SetType.NORMAL, savedExercises.get(0).getSets().get(1).getSetType());
        assertEquals(SetType.FAILURE, savedExercises.get(0).getSets().get(2).getSetType());
        assertEquals(1, savedExercises.get(1).getOrderIndex());
        assertEquals(7, savedExercises.get(1).getExercise().getId());
        assertEquals(1, savedExercises.get(1).getSets().size());
        assertEquals(4, savedSets.size());
    }

    @Test
    void startWorkoutWithNullTemplateExercisesSkipsChildEntitySaves() {
        final WorkoutTemplate template = new WorkoutTemplate();
        template.setExercises(null);
        final User user = new User();
        final WorkoutSession saved = new WorkoutSession();
        saved.setId(99);
        saved.setExercises(new ArrayList<>());

        when(workoutTemplateService.getWorkoutTemplateById(10, "user@mail.com")).thenReturn(template);
        when(userService.getUserByEmail("user@mail.com")).thenReturn(user);
        when(workoutSessionRepository.saveAndFlush(any(WorkoutSession.class))).thenReturn(saved);

        final Integer sessionId = workoutSessionService.startWorkout(10, "user@mail.com");

        assertEquals(99, sessionId);
        assertEquals(0, saved.getExercises().size());
        verify(sessionExerciseRepository, never()).saveAll(anyList());
        verify(exerciseSetRepository, never()).saveAll(anyList());
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

        when(exerciseSetRepository.findAllByIdInAndSessionExercise_Session_User_Email(List.of(11, 12), "user@mail.com"))
                .thenReturn(List.of(firstSet, secondSet));

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
        verify(exerciseSetRepository).saveAll(List.of(firstSet, secondSet));
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
