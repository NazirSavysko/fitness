package fitness.app.project.fitnessapp.service.impl;

import fitness.app.project.fitnessapp.model.ExerciseDefinition;
import fitness.app.project.fitnessapp.model.TemplateExercise;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import fitness.app.project.fitnessapp.repository.WorkoutTemplateRepository;
import fitness.app.project.fitnessapp.service.ExerciseDefinitionService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WorkoutTemplateServiceImplTest {

    private final WorkoutTemplateRepository workoutTemplateRepository = mock(WorkoutTemplateRepository.class);
    private final ExerciseDefinitionService exerciseDefinitionService = mock(ExerciseDefinitionService.class);
    private final WorkoutTemplateServiceImpl workoutTemplateService = new WorkoutTemplateServiceImpl(
            workoutTemplateRepository,
            exerciseDefinitionService
    );

    @Test
    void buildTemplateExercisesAssignsOrderIndexByIncomingExerciseIdOrder() {
        final WorkoutTemplate workoutTemplate = new WorkoutTemplate();
        when(exerciseDefinitionService.getReferenceById(3)).thenReturn(exercise(3));
        when(exerciseDefinitionService.getReferenceById(1)).thenReturn(exercise(1));
        when(exerciseDefinitionService.getReferenceById(2)).thenReturn(exercise(2));

        final List<TemplateExercise> result = workoutTemplateService.buildTemplateExercises(workoutTemplate, List.of(3, 1, 2));

        assertEquals(3, result.size());
        assertSame(workoutTemplate, result.get(0).getTemplate());
        assertSame(workoutTemplate, result.get(1).getTemplate());
        assertSame(workoutTemplate, result.get(2).getTemplate());
        assertEquals(3, result.get(0).getExercise().getId());
        assertEquals(0, result.get(0).getOrderIndex());
        assertEquals(1, result.get(1).getExercise().getId());
        assertEquals(1, result.get(1).getOrderIndex());
        assertEquals(2, result.get(2).getExercise().getId());
        assertEquals(2, result.get(2).getOrderIndex());
    }

    private static ExerciseDefinition exercise(final int id) {
        final ExerciseDefinition exerciseDefinition = new ExerciseDefinition();
        exerciseDefinition.setId(id);
        return exerciseDefinition;
    }
}
