package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.WorkoutHistoryCardDTO;
import fitness.app.project.fitnessapp.model.ExerciseSet;
import fitness.app.project.fitnessapp.model.SessionExercise;
import fitness.app.project.fitnessapp.model.TemplateExercise;
import fitness.app.project.fitnessapp.model.WorkoutSession;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import fitness.app.project.fitnessapp.service.WorkoutSessionService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WorkoutFacadeImplTest {

    private final WorkoutSessionService workoutSessionService = mock(WorkoutSessionService.class);
    private final WorkoutFacadeImpl workoutFacade = new WorkoutFacadeImpl(workoutSessionService);

    @Test
    void getHistoryCalculatesCompletionPercentageFromTemplateAndCompletedSets() {
        final WorkoutTemplate template = new WorkoutTemplate();
        final TemplateExercise firstTemplateExercise = new TemplateExercise();
        firstTemplateExercise.setNormalSets(2);
        firstTemplateExercise.setFailureSets(1);
        template.setExercises(List.of(firstTemplateExercise));
        template.setName("Push Day");

        final ExerciseSet completedSet = new ExerciseSet();
        completedSet.setWeight(new BigDecimal("80"));
        completedSet.setReps(8);
        final ExerciseSet incompleteSet = new ExerciseSet();
        incompleteSet.setWeight(BigDecimal.ZERO);
        incompleteSet.setReps(0);

        final SessionExercise sessionExercise = new SessionExercise();
        sessionExercise.setSets(List.of(completedSet, incompleteSet));

        final WorkoutSession workoutSession = new WorkoutSession();
        workoutSession.setId(5);
        workoutSession.setSourceTemplate(template);
        workoutSession.setStartedAt(LocalDateTime.of(2026, 3, 16, 10, 0));
        workoutSession.setEndedAt(LocalDateTime.of(2026, 3, 16, 11, 0));
        workoutSession.setExercises(List.of(sessionExercise));

        when(workoutSessionService.getHistory(eq("user@mail.com"), isNull(), eq("ALL"), eq("DATE_DESC"), eq(PageRequest.of(0, 10))))
                .thenReturn(new PageImpl<>(List.of(workoutSession)));

        final Page<WorkoutHistoryCardDTO> history = workoutFacade.getHistory("user@mail.com", null, "ALL", "DATE_DESC", PageRequest.of(0, 10));

        assertEquals(1, history.getContent().size());
        assertEquals(33, history.getContent().get(0).completionPercentage());
        assertEquals("2026-03-16", history.getContent().get(0).dateKey());
    }
}
