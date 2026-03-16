package fitness.app.project.fitnessapp.service.impl;

import fitness.app.project.fitnessapp.dto.TemplateExerciseConfigDTO;
import fitness.app.project.fitnessapp.model.ExerciseDefinition;
import fitness.app.project.fitnessapp.model.TemplateExercise;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import fitness.app.project.fitnessapp.repository.WorkoutTemplateRepository;
import fitness.app.project.fitnessapp.service.ExerciseDefinitionService;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
                        new TemplateExerciseConfigDTO(3, 3, 1),
                        new TemplateExerciseConfigDTO(1, 2, 0),
                        new TemplateExerciseConfigDTO(2, 1, 2)
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
        assertEquals(2, result.get(2).getExercise().getId());
        assertEquals(2, result.get(2).getOrderIndex());
        assertEquals(1, result.get(2).getNormalSets());
        assertEquals(2, result.get(2).getFailureSets());
    }

    private static ExerciseDefinition exercise(final int id) {
        final ExerciseDefinition exerciseDefinition = new ExerciseDefinition();
        exerciseDefinition.setId(id);
        return exerciseDefinition;
    }

    @Test
    void validateScheduledDayConflictsThrowsWhenAnotherTemplateUsesSameDay() {
        final WorkoutTemplate existing = new WorkoutTemplate();
        existing.setId(5);
        existing.setScheduledDay(DayOfWeek.MONDAY);
        when(workoutTemplateRepository.findAllByUser_Email("user@mail.com")).thenReturn(List.of(existing));

        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> workoutTemplateService.validateScheduledDayConflicts(
                        "user@mail.com",
                        DayOfWeek.MONDAY,
                        null
                )
        );

        assertEquals("Day conflict: You already have a template scheduled for MONDAY.", exception.getMessage());
    }

    @Test
    void validateScheduledDayConflictsIgnoresCurrentTemplateDuringUpdate() {
        final WorkoutTemplate existing = new WorkoutTemplate();
        existing.setId(7);
        existing.setScheduledDay(DayOfWeek.MONDAY);
        when(workoutTemplateRepository.findAllByUser_Email("user@mail.com")).thenReturn(List.of(existing));

        assertDoesNotThrow(() -> workoutTemplateService.validateScheduledDayConflicts(
                "user@mail.com",
                DayOfWeek.MONDAY,
                7
        ));
    }
}
