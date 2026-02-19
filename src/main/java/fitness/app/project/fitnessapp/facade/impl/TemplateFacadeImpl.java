package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.*;
import fitness.app.project.fitnessapp.facade.TemplateFacade;
import fitness.app.project.fitnessapp.mapper.ExerciseDefinitionMapper;
import fitness.app.project.fitnessapp.mapper.GetTemplateForDashboardMapper;
import fitness.app.project.fitnessapp.mapper.TemplateWorkoutMapper;
import fitness.app.project.fitnessapp.model.ExerciseDefinition;
import fitness.app.project.fitnessapp.model.TemplateExercise;
import fitness.app.project.fitnessapp.model.User;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import fitness.app.project.fitnessapp.service.ExerciseDefinitionService;
import fitness.app.project.fitnessapp.service.UserService;
import fitness.app.project.fitnessapp.service.WorkoutTemplateService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static fitness.app.project.fitnessapp.utils.MapperUtils.mapList;

@Component
@AllArgsConstructor
public final class TemplateFacadeImpl implements TemplateFacade {
    private final UserService userService;
    private final ExerciseDefinitionMapper exerciseDefinitionMapper;
    private final TemplateWorkoutMapper updateTemplateExerciseMapper;
    private final WorkoutTemplateService workoutTemplateService;
    private final TemplateWorkoutMapper getTemplateWorkoutMapper;
    private final ExerciseDefinitionService exerciseDefinitionService;
    private final GetTemplateForDashboardMapper getTemplateForDashboardMapper;

    public @NonNull @Unmodifiable List<GetTemplateDTO> getTemplatesByEmail(final String email) {
        final List<WorkoutTemplate> templates = this.workoutTemplateService.getTemplatesByUserEmail(email);

        return mapList(templates, this.getTemplateWorkoutMapper);
    }

    @Override
    public void deleteTemplateByIdAndUserEmail(final Integer templateId, final String name) {
        this.workoutTemplateService.deleteTemplateByIdAndUserEmail(templateId, name);
    }

    @Override
    public void updateTemplate(final @NonNull UpdateTemplateDTO templateDTO, final String email) {
        final WorkoutTemplate workoutTemplate = this.workoutTemplateService.getWorkoutTemplateById(templateDTO.id(), email);

        workoutTemplate.setName(templateDTO.name());
        final List<TemplateExercise> exercises = this.createTemplateExercises(templateDTO.exercises());
        exercises.forEach(ex -> ex.setTemplate(workoutTemplate));
        workoutTemplate.setExercises(exercises);

        this.workoutTemplateService.saveWorkout(workoutTemplate);
    }

    @Override
    public GetTemplateDTO getTemplateForUpdateById(final Integer templateId, final String email) {
        final WorkoutTemplate workoutTemplate = this.workoutTemplateService.getWorkoutTemplateById(templateId, email);

        return this.updateTemplateExerciseMapper.mapEntityToDto(workoutTemplate);
    }

    @Override
    public @Unmodifiable @NonNull List<TemplateExerciseDTO> getExerciseDefinitions() {
        final List<ExerciseDefinition> exerciseDefinitions = this.exerciseDefinitionService.getAllExerciseDefinitions();

        return mapList(exerciseDefinitions, exerciseDefinitionMapper);
    }

    @Override
    public void createTemplate(final @NonNull CreateTemplateDTO createTemplateDTO, final String email) {
        final User user = this.userService.getUserByEmail(email);
        final WorkoutTemplate workoutTemplate = new WorkoutTemplate();
        workoutTemplate.setName(createTemplateDTO.name());
        workoutTemplate.setUser(user);
        final List<TemplateExercise> exercises = this.createTemplateExercises(createTemplateDTO.exercises());
        exercises.forEach(ex -> ex.setTemplate(workoutTemplate));
        workoutTemplate.setExercises(exercises);

        this.workoutTemplateService.saveWorkout(workoutTemplate);
    }

    @Override
    public @NonNull @Unmodifiable List<GetDashboardTemplateDTO> getDashboardTemplates(final String email) {
        final List<WorkoutTemplate> templates = this.workoutTemplateService.getTemplatesByUserEmail(email);

        return mapList(templates, this.getTemplateForDashboardMapper);
    }

    private @NonNull List<TemplateExercise> createTemplateExercises(final @NonNull List<TemplateExerciseDTO> exerciseDTOs) {
        final List<TemplateExercise> exercises = new ArrayList<>();
        for (int i = 0; i < exerciseDTOs.size(); i++) {
            final TemplateExercise templateExercise = new TemplateExercise();
            templateExercise.setExercise(this.exerciseDefinitionService.getReferenceById(exerciseDTOs.get(i).id()));
            templateExercise.setOrderIndex(i);
            exercises.add(templateExercise);
        }
        return exercises;
    }
}
