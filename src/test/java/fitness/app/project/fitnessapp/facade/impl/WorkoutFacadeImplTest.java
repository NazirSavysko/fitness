package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.WorkoutHistoryCardDTO;
import fitness.app.project.fitnessapp.model.ExerciseDefinition;
import fitness.app.project.fitnessapp.model.ExerciseSet;
import fitness.app.project.fitnessapp.model.SessionExercise;
import fitness.app.project.fitnessapp.model.TemplateExercise;
import fitness.app.project.fitnessapp.model.WorkoutSession;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import fitness.app.project.fitnessapp.service.WorkoutSessionService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WorkoutFacadeImplTest {

    private final WorkoutSessionService workoutSessionService = mock(WorkoutSessionService.class);
    private final WorkoutFacadeImpl workoutFacade = new WorkoutFacadeImpl(workoutSessionService);

    @Test
    void getHistoryCalculatesCompletionPercentageFromTemplateAndCompletedSets() {
        final WorkoutTemplate template = new WorkoutTemplate();
        template.setName("Push Day");
        template.setExercises(List.of(templateExercise(2, 1), templateExercise(1, 0)));

        final WorkoutSession session = new WorkoutSession();
        session.setId(9);
        session.setSourceTemplate(template);
        session.setStartedAt(LocalDateTime.now().minusMinutes(30));
        session.setEndedAt(LocalDateTime.now());
        session.setExercises(List.of(sessionExerciseWithSets()));

        when(workoutSessionService.getHistory(any(), any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(List.of(session), PageRequest.of(0, 10), 1));

        final WorkoutHistoryCardDTO dto = workoutFacade
                .getHistory("user@mail.com", null, null, null, PageRequest.of(0, 10))
                .getContent()
                .get(0);

        assertEquals(50, dto.completionPercentage());
    }

    private static TemplateExercise templateExercise(final int normalSets, final int failureSets) {
        final TemplateExercise exercise = new TemplateExercise();
        exercise.setNormalSets(normalSets);
        exercise.setFailureSets(failureSets);
        return exercise;
    }

    private static SessionExercise sessionExerciseWithSets() {
        final ExerciseDefinition definition = new ExerciseDefinition();
        definition.setMuscleGroup("Chest");

        final SessionExercise exercise = new SessionExercise();
        exercise.setExercise(definition);
        exercise.setSets(List.of(
                exerciseSet(new BigDecimal("40"), 10),
                exerciseSet(new BigDecimal("45"), 8),
                exerciseSet(null, 12)
        ));
        return exercise;
    }

    private static ExerciseSet exerciseSet(final BigDecimal weight, final Integer reps) {
        final ExerciseSet set = new ExerciseSet();
        set.setWeight(weight);
        set.setReps(reps);
        return set;
    }
}
