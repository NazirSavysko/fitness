package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.CreateTemplateDTO;
import fitness.app.project.fitnessapp.dto.UpdateTemplateDTO;
import fitness.app.project.fitnessapp.mapper.ExerciseDefinitionMapper;
import fitness.app.project.fitnessapp.mapper.GetTemplateForDashboardMapper;
import fitness.app.project.fitnessapp.mapper.TemplateWorkoutMapper;
import fitness.app.project.fitnessapp.model.User;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import fitness.app.project.fitnessapp.service.ExerciseDefinitionService;
import fitness.app.project.fitnessapp.service.UserService;
import fitness.app.project.fitnessapp.service.WorkoutTemplateService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TemplateFacadeImplTest {

    private final UserService userService = mock(UserService.class);
    private final ExerciseDefinitionMapper exerciseDefinitionMapper = mock(ExerciseDefinitionMapper.class);
    private final TemplateWorkoutMapper updateTemplateWorkoutMapper = mock(TemplateWorkoutMapper.class);
    private final TemplateWorkoutMapper getTemplateWorkoutMapper = mock(TemplateWorkoutMapper.class);
    private final WorkoutTemplateService workoutTemplateService = mock(WorkoutTemplateService.class);
    private final ExerciseDefinitionService exerciseDefinitionService = mock(ExerciseDefinitionService.class);
    private final GetTemplateForDashboardMapper getTemplateForDashboardMapper = mock(GetTemplateForDashboardMapper.class);

    private final TemplateFacadeImpl templateFacade = new TemplateFacadeImpl(
            userService,
            exerciseDefinitionMapper,
            updateTemplateWorkoutMapper,
            workoutTemplateService,
            getTemplateWorkoutMapper,
            exerciseDefinitionService,
            getTemplateForDashboardMapper
    );

    @Test
    void createTemplateDelegatesExerciseIdsToService() {
        final User user = new User();
        final List<Integer> exerciseIds = List.of(1, 2, 3);
        when(userService.getUserByEmail("user@mail.com")).thenReturn(user);
        when(workoutTemplateService.buildTemplateExercises(any(WorkoutTemplate.class), eq(exerciseIds)))
                .thenReturn(List.of());

        templateFacade.createTemplate(
                new CreateTemplateDTO(
                        "Back day",
                        exerciseIds
                ),
                "user@mail.com"
        );

        final ArgumentCaptor<WorkoutTemplate> templateCaptor = ArgumentCaptor.forClass(WorkoutTemplate.class);
        verify(workoutTemplateService).buildTemplateExercises(templateCaptor.capture(), eq(exerciseIds));
        verify(workoutTemplateService).saveWorkout(templateCaptor.capture());
        final WorkoutTemplate builtTemplate = templateCaptor.getAllValues().get(0);
        final WorkoutTemplate savedTemplate = templateCaptor.getAllValues().get(1);

        assertSame(user, builtTemplate.getUser());
        assertSame(builtTemplate, savedTemplate);
        assertEquals("Back day", savedTemplate.getName());
        assertTrue(savedTemplate.getExercises().isEmpty());
    }

    @Test
    void updateTemplateReplacesExerciseListUsingServiceBuiltCollection() {
        final WorkoutTemplate existingTemplate = new WorkoutTemplate();
        existingTemplate.setName("Old name");
        existingTemplate.setExercises(List.of());
        final List<Integer> exerciseIds = List.of(2, 1);

        when(workoutTemplateService.getWorkoutTemplateById(55, "user@mail.com")).thenReturn(existingTemplate);
        when(workoutTemplateService.buildTemplateExercises(existingTemplate, exerciseIds)).thenReturn(List.of());

        templateFacade.updateTemplate(
                new UpdateTemplateDTO(
                        55,
                        "Back day updated",
                        exerciseIds
                ),
                "user@mail.com"
        );

        verify(workoutTemplateService).buildTemplateExercises(existingTemplate, exerciseIds);
        verify(workoutTemplateService).saveWorkout(existingTemplate);

        assertEquals("Back day updated", existingTemplate.getName());
        assertTrue(existingTemplate.getExercises().isEmpty());
    }
}
