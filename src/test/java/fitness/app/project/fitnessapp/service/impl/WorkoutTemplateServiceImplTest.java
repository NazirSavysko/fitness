package fitness.app.project.fitnessapp.service.impl;

import fitness.app.project.fitnessapp.dto.TemplateExerciseConfigDTO;
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
    void buildTemplateExercisesAssignsOrderIndexAndSetConfiguration() {
        final WorkoutTemplate workoutTemplate = new WorkoutTemplate();
        when(exerciseDefinitionService.getReferenceById(3)).thenReturn(exercise(3));
        when(exerciseDefinitionService.getReferenceById(1)).thenReturn(exercise(1));
        when(exerciseDefinitionService.getReferenceById(2)).thenReturn(exercise(2));

        final List<TemplateExercise> result = workoutTemplateService.buildTemplateExercises(
                workoutTemplate,
                List.of(
                        new TemplateExerciseConfigDTO(3, 3, 1, 90),
                        new TemplateExerciseConfigDTO(1, 2, 0, 60),
                        new TemplateExerciseConfigDTO(2, 1, 2, 120)
                )
        );

        assertEquals(3, result.size());
        assertSame(workoutTemplate, result.get(0).getTemplate());
        assertSame(workoutTemplate, result.get(1).getTemplate());
        assertSame(workoutTemplate, result.get(2).getTemplate());
        assertEquals(3, result.get(0).getExercise().getId());
        assertEquals(0, result.get(0).getOrderIndex());
        assertEquals(1, result.get(1).getExercise().getId());
        assertEquals(1, result.get(1).getOrderIndex());
        assertEquals(2, result.get(1).getNormalSets());
        assertEquals(0, result.get(1).getFailureSets());
        assertEquals(60, result.get(1).getRestSeconds());
        assertEquals(2, result.get(2).getExercise().getId());
        assertEquals(2, result.get(2).getOrderIndex());
        assertEquals(1, result.get(2).getNormalSets());
        assertEquals(2, result.get(2).getFailureSets());
        assertEquals(120, result.get(2).getRestSeconds());
    }

    private static ExerciseDefinition exercise(final int id) {
        final ExerciseDefinition exerciseDefinition = new ExerciseDefinition();
        exerciseDefinition.setId(id);
        return exerciseDefinition;
    }
}
