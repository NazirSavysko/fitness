package fitness.app.project.fitnessapp.mapper.impl;

import fitness.app.project.fitnessapp.dto.TemplateExerciseDTO;
import fitness.app.project.fitnessapp.mapper.ExerciseDefinitionMapper;
import fitness.app.project.fitnessapp.model.ExerciseDefinition;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ExerciseDefinitionMapperImpl implements ExerciseDefinitionMapper {
    @Override
    public TemplateExerciseDTO mapEntityToDto(final @NonNull ExerciseDefinition exerciseDefinition) {
        return new TemplateExerciseDTO(
                exerciseDefinition.getId(),
                exerciseDefinition.getName(),
                exerciseDefinition.getMuscleGroup(),
                null,
                null,
                null
        );
    }
}
