package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.CreateTemplateDTO;
import fitness.app.project.fitnessapp.dto.TemplateExerciseDTO;
import fitness.app.project.fitnessapp.dto.UpdateTemplateDTO;
import fitness.app.project.fitnessapp.mapper.ExerciseDefinitionMapper;
import fitness.app.project.fitnessapp.mapper.GetTemplateForDashboardMapper;
import fitness.app.project.fitnessapp.mapper.TemplateWorkoutMapper;
import fitness.app.project.fitnessapp.model.ExerciseDefinition;
import fitness.app.project.fitnessapp.model.FitnessUser;
import fitness.app.project.fitnessapp.model.TemplateExercise;
import fitness.app.project.fitnessapp.model.User;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import fitness.app.project.fitnessapp.service.ExerciseDefinitionService;
import fitness.app.project.fitnessapp.service.FitnessUserService;
import fitness.app.project.fitnessapp.service.WorkoutTemplateService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TemplateFacadeImplTest {

    private final FitnessUserService fitnessUserService = mock(FitnessUserService.class);
    private final ExerciseDefinitionMapper exerciseDefinitionMapper = mock(ExerciseDefinitionMapper.class);
    private final TemplateWorkoutMapper updateTemplateWorkoutMapper = mock(TemplateWorkoutMapper.class);
    private final TemplateWorkoutMapper getTemplateWorkoutMapper = mock(TemplateWorkoutMapper.class);
    private final WorkoutTemplateService workoutTemplateService = mock(WorkoutTemplateService.class);
    private final ExerciseDefinitionService exerciseDefinitionService = mock(ExerciseDefinitionService.class);
    private final GetTemplateForDashboardMapper getTemplateForDashboardMapper = mock(GetTemplateForDashboardMapper.class);

    private final TemplateFacadeImpl templateFacade = new TemplateFacadeImpl(
            fitnessUserService,
            exerciseDefinitionMapper,
            updateTemplateWorkoutMapper,
            workoutTemplateService,
            getTemplateWorkoutMapper,
            exerciseDefinitionService,
            getTemplateForDashboardMapper
    );

    @Test
    void createTemplatePreservesExerciseOrder() {
        final User user = new User();
        final FitnessUser fitnessUser = new FitnessUser();
        fitnessUser.setUserDetails(user);

        when(fitnessUserService.getFitnessUserByEmail("user@mail.com")).thenReturn(fitnessUser);
        when(exerciseDefinitionService.getReferenceById(1)).thenReturn(exercise(1, "Pull-up"));
        when(exerciseDefinitionService.getReferenceById(2)).thenReturn(exercise(2, "Barbell row"));
        when(exerciseDefinitionService.getReferenceById(3)).thenReturn(exercise(3, "Biceps curl"));

        templateFacade.createTemplate(
                new CreateTemplateDTO(
                        "Back day",
                        List.of(
                                new TemplateExerciseDTO(1, "Pull-up"),
                                new TemplateExerciseDTO(2, "Barbell row"),
                                new TemplateExerciseDTO(3, "Biceps curl")
                        )
                ),
                "user@mail.com"
        );

        final ArgumentCaptor<WorkoutTemplate> templateCaptor = ArgumentCaptor.forClass(WorkoutTemplate.class);
        verify(workoutTemplateService).saveWorkout(templateCaptor.capture());
        final WorkoutTemplate savedTemplate = templateCaptor.getValue();

        assertSame(user, savedTemplate.getUser());
        assertExercise(savedTemplate.getExercises().get(0), savedTemplate, 1, 0);
        assertExercise(savedTemplate.getExercises().get(1), savedTemplate, 2, 1);
        assertExercise(savedTemplate.getExercises().get(2), savedTemplate, 3, 2);
    }

    @Test
    void updateTemplateReplacesExerciseListInRequestOrder() {
        final WorkoutTemplate existingTemplate = new WorkoutTemplate();
        existingTemplate.setName("Old name");
        existingTemplate.setExercises(List.of());

        when(workoutTemplateService.getWorkoutTemplateById(55, "user@mail.com")).thenReturn(existingTemplate);
        when(exerciseDefinitionService.getReferenceById(2)).thenReturn(exercise(2, "Barbell row"));
        when(exerciseDefinitionService.getReferenceById(1)).thenReturn(exercise(1, "Pull-up"));

        templateFacade.updateTemplate(
                new UpdateTemplateDTO(
                        55,
                        "Back day updated",
                        List.of(
                                new TemplateExerciseDTO(2, "Barbell row"),
                                new TemplateExerciseDTO(1, "Pull-up")
                        )
                ),
                "user@mail.com"
        );

        final ArgumentCaptor<WorkoutTemplate> templateCaptor = ArgumentCaptor.forClass(WorkoutTemplate.class);
        verify(workoutTemplateService).saveWorkout(templateCaptor.capture());
        final WorkoutTemplate savedTemplate = templateCaptor.getValue();

        assertEquals("Back day updated", savedTemplate.getName());
        assertExercise(savedTemplate.getExercises().get(0), savedTemplate, 2, 0);
        assertExercise(savedTemplate.getExercises().get(1), savedTemplate, 1, 1);
    }

    private static ExerciseDefinition exercise(final int id, final String name) {
        final ExerciseDefinition exerciseDefinition = new ExerciseDefinition();
        exerciseDefinition.setId(id);
        exerciseDefinition.setName(name);

        return exerciseDefinition;
    }

    private static void assertExercise(final TemplateExercise templateExercise,
                                       final WorkoutTemplate workoutTemplate,
                                       final int expectedExerciseId,
                                       final int expectedOrderIndex) {
        assertSame(workoutTemplate, templateExercise.getTemplate());
        assertEquals(expectedExerciseId, templateExercise.getExercise().getId());
        assertEquals(expectedOrderIndex, templateExercise.getOrderIndex());
    }
}
