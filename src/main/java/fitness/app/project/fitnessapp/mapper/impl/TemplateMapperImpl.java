package fitness.app.project.fitnessapp.mapper.impl;

import fitness.app.project.fitnessapp.dto.GetTemplateDTO;
import fitness.app.project.fitnessapp.mapper.GetTemplateExerciseMapper;
import fitness.app.project.fitnessapp.mapper.TemplateWorkoutMapper;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.util.Comparator;

import static fitness.app.project.fitnessapp.utils.MapperUtils.mapList;

@Component
@AllArgsConstructor
public class TemplateMapperImpl implements TemplateWorkoutMapper {
    private final GetTemplateExerciseMapper getTemplateExerciseMapper;

    @Override
    public GetTemplateDTO mapEntityToDto(final @NonNull WorkoutTemplate workoutTemplate) {
        return new GetTemplateDTO(
                workoutTemplate.getId(),
                workoutTemplate.getName(),
                mapList(workoutTemplate.getExercises().stream()
                                .sorted(Comparator.comparing(templateExercise -> templateExercise.getOrderIndex(), Comparator.nullsLast(Integer::compareTo)))
                                .toList(),
                        this.getTemplateExerciseMapper)
        );
    }
}
