package fitness.app.project.fitnessapp.mapper.impl;

import fitness.app.project.fitnessapp.dto.GetDashboardTemplateDTO;
import fitness.app.project.fitnessapp.mapper.GetTemplateForDashboardMapper;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class GetTemplateForDashboardMapperImpl implements GetTemplateForDashboardMapper {
    @Override
    public GetDashboardTemplateDTO mapEntityToDto(final @NonNull WorkoutTemplate workoutTemplate) {
        return new GetDashboardTemplateDTO(
            workoutTemplate.getTemplateId(),
            workoutTemplate.getName()
        );
    }
}
