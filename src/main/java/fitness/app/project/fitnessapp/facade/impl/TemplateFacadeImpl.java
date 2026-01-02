package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.GetTemplateDTO;
import fitness.app.project.fitnessapp.facade.TemplateFacade;
import fitness.app.project.fitnessapp.mapper.GetTemplateWorkoutMapper;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;
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
    private final WorkoutTemplateService workoutTemplateService;
    private final GetTemplateWorkoutMapper getTemplateWorkoutMapper;

    public @NonNull @Unmodifiable List<GetTemplateDTO> getTemplatesByEmail(final String email) {
        final List<WorkoutTemplate> templates = this.workoutTemplateService.getTemplatesByUserEmail(email);

        return mapList(templates, this.getTemplateWorkoutMapper);
    }

    @Override
    public void deleteTemplateByIdAndUserEmail(final Integer templateId, final String name) {
        this.workoutTemplateService.deleteTemplateByIdAndUserEmail(templateId, name);
    }
}
