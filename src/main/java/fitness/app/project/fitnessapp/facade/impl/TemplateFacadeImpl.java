package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.GetTemplateDTO;
import fitness.app.project.fitnessapp.dto.TemplateExerciseDTO;
import fitness.app.project.fitnessapp.dto.UpdateTemplateDTO;
import fitness.app.project.fitnessapp.facade.TemplateFacade;
import fitness.app.project.fitnessapp.mapper.ExerciseDefinitionMapper;
import fitness.app.project.fitnessapp.mapper.TemplateWorkoutMapper;
import fitness.app.project.fitnessapp.model.ExerciseDefinition;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import fitness.app.project.fitnessapp.service.ExerciseDefinitionService;
import fitness.app.project.fitnessapp.service.WorkoutTemplateService;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

import static fitness.app.project.fitnessapp.utils.MapperUtils.mapList;

@Component
@AllArgsConstructor
public final class TemplateFacadeImpl implements TemplateFacade {
    private final ExerciseDefinitionMapper exerciseDefinitionMapper;
    private final TemplateWorkoutMapper updateTemplateExerciseMapper;
    private final WorkoutTemplateService workoutTemplateService;
    private final TemplateWorkoutMapper getTemplateWorkoutMapper;
    private final ExerciseDefinitionService exerciseDefinitionService;

    public @NonNull @Unmodifiable List<GetTemplateDTO> getTemplatesByEmail(final String email) {
        final List<WorkoutTemplate> templates = this.workoutTemplateService.getTemplatesByUserEmail(email);

        return mapList(templates, this.getTemplateWorkoutMapper);
    }

    @Override
    public void deleteTemplateByIdAndUserEmail(final Integer templateId, final String name) {
        this.workoutTemplateService.deleteTemplateByIdAndUserEmail(templateId, name);
    }

    @Override
    public void updateTemplate(final UpdateTemplateDTO templateDTO, final String email) {
        final WorkoutTemplate workoutTemplate = this.workoutTemplateService.getWorkoutTemplateById(templateDTO.id(), email);

        workoutTemplate.setName(templateDTO.name());
        final List<ExerciseDefinition> realExercises = templateDTO.exercises().stream()
                .map(dto -> exerciseDefinitionService.getReferenceById(dto.id()))
                .toList();

        workoutTemplate.setExercises(realExercises);

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
}
