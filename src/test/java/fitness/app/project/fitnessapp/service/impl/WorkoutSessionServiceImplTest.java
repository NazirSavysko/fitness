package fitness.app.project.fitnessapp.service.impl;

import fitness.app.project.fitnessapp.dto.AddSetDTO;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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
    void startWorkoutCopiesTemplateExercisesWithOrder() {
        final WorkoutTemplate template = new WorkoutTemplate();
        template.setExercises(List.of(templateExercise(4, 0), templateExercise(7, 1)));
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
        assertEquals(1, created.getExercises().get(1).getOrderIndex());
        assertEquals(7, created.getExercises().get(1).getExercise().getId());
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
    void getHistoryBuildsPageRequestWithDateDescByDefault() {
        when(workoutSessionRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of()));

        workoutSessionService.getHistory("user@mail.com", null, "ALL", "DATE_DESC", PageRequest.of(1, 10));

        final ArgumentCaptor<PageRequest> captor = ArgumentCaptor.forClass(PageRequest.class);
        verify(workoutSessionRepository).findAll(any(Specification.class), captor.capture());
        final PageRequest pageRequest = captor.getValue();
        assertEquals(1, pageRequest.getPageNumber());
        assertEquals(10, pageRequest.getPageSize());
        assertEquals("startedAt: DESC", pageRequest.getSort().toString());
    }

    @Test
    void getHistoryBuildsPageRequestWithDurationSort() {
        when(workoutSessionRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(new PageImpl<>(List.of()));

        workoutSessionService.getHistory("user@mail.com", 3L, "LAST_30_DAYS", "DURATION_DESC", PageRequest.of(0, 10));

        final ArgumentCaptor<Specification<WorkoutSession>> specificationCaptor = ArgumentCaptor.forClass(Specification.class);
        final ArgumentCaptor<PageRequest> pageCaptor = ArgumentCaptor.forClass(PageRequest.class);
        verify(workoutSessionRepository).findAll(specificationCaptor.capture(), pageCaptor.capture());
        assertNotNull(specificationCaptor.getValue());
        assertEquals("endedAt - startedAt: DESC", pageCaptor.getValue().getSort().toString());
    }

    private static TemplateExercise templateExercise(final int exerciseId, final int orderIndex) {
        final ExerciseDefinition exerciseDefinition = new ExerciseDefinition();
        exerciseDefinition.setId(exerciseId);
        final TemplateExercise templateExercise = new TemplateExercise();
        templateExercise.setExercise(exerciseDefinition);
        templateExercise.setOrderIndex(orderIndex);
        return templateExercise;
    }
}
