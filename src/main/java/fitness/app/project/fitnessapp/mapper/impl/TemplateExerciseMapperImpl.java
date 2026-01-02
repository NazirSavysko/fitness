package fitness.app.project.fitnessapp.mapper.impl;

import fitness.app.project.fitnessapp.dto.TemplateExerciseDTO;
import fitness.app.project.fitnessapp.mapper.GetTemplateExerciseMapper;
import fitness.app.project.fitnessapp.model.ExerciseDefinition;
import org.springframework.stereotype.Component;

@Component
public class TemplateExerciseMapperImpl implements GetTemplateExerciseMapper {
    @Override
    public TemplateExerciseDTO mapEntityToDto(final ExerciseDefinition exerciseDefinition) {
        return new TemplateExerciseDTO(
                exerciseDefinition.getExerciseDefId(),
                exerciseDefinition.getName()
        );
    }
}
