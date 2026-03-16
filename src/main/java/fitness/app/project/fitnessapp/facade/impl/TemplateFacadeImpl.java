package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.*;
import fitness.app.project.fitnessapp.facade.TemplateFacade;
import fitness.app.project.fitnessapp.mapper.ExerciseDefinitionMapper;
import fitness.app.project.fitnessapp.mapper.GetTemplateForDashboardMapper;
import fitness.app.project.fitnessapp.mapper.TemplateWorkoutMapper;
import fitness.app.project.fitnessapp.model.ExerciseDefinition;
import fitness.app.project.fitnessapp.model.User;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import fitness.app.project.fitnessapp.service.ExerciseDefinitionService;
import fitness.app.project.fitnessapp.service.UserService;
import fitness.app.project.fitnessapp.service.WorkoutTemplateService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        this.workoutTemplateService.validateScheduledDayConflicts(email, templateDTO.scheduledDays(), templateDTO.id());

        workoutTemplate.setName(templateDTO.name());
        workoutTemplate.setScheduledDays(safeScheduledDays(templateDTO.scheduledDays()));
        workoutTemplate.getExercises().clear();
        workoutTemplate.getExercises().addAll(this.workoutTemplateService.buildTemplateExercises(workoutTemplate, templateDTO.exercises()));

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
        this.workoutTemplateService.validateScheduledDayConflicts(email, createTemplateDTO.scheduledDays(), null);
        final WorkoutTemplate workoutTemplate = new WorkoutTemplate();
        workoutTemplate.setName(createTemplateDTO.name());
        workoutTemplate.setUser(user);
        workoutTemplate.setScheduledDays(safeScheduledDays(createTemplateDTO.scheduledDays()));
        workoutTemplate.setExercises(this.workoutTemplateService.buildTemplateExercises(workoutTemplate, createTemplateDTO.exercises()));

        this.workoutTemplateService.saveWorkout(workoutTemplate);
    }

    @Override
    public @NonNull @Unmodifiable List<GetDashboardTemplateDTO> getDashboardTemplates(final String email) {
        final List<WorkoutTemplate> templates = this.workoutTemplateService.getTemplatesByUserEmail(email);

        return mapList(templates, this.getTemplateForDashboardMapper);
    }

    private static Set<DayOfWeek> safeScheduledDays(final Set<DayOfWeek> scheduledDays) {
        return scheduledDays == null ? new HashSet<>() : new HashSet<>(scheduledDays);
    }

}
