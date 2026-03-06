package fitness.app.project.fitnessapp.mapper.impl;

import fitness.app.project.fitnessapp.dto.TemplateExerciseDTO;
import fitness.app.project.fitnessapp.mapper.GetTemplateExerciseMapper;
import fitness.app.project.fitnessapp.model.TemplateExercise;
import org.springframework.stereotype.Component;

@Component
public class TemplateExerciseMapperImpl implements GetTemplateExerciseMapper {
    @Override
    public TemplateExerciseDTO mapEntityToDto(final TemplateExercise templateExercise) {
        return new TemplateExerciseDTO(
                templateExercise.getExercise().getId(),
                templateExercise.getExercise().getName(),
                templateExercise.getExercise().getMuscleGroup(),
                templateExercise.getOrderIndex(),
                templateExercise.getNormalSets(),
                templateExercise.getFailureSets(),
                templateExercise.getRestSeconds()
        );
    }
}
