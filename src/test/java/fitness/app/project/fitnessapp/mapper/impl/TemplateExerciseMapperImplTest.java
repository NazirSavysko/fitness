package fitness.app.project.fitnessapp.mapper.impl;

import fitness.app.project.fitnessapp.dto.TemplateExerciseDTO;
import fitness.app.project.fitnessapp.model.ExerciseDefinition;
import fitness.app.project.fitnessapp.model.TemplateExercise;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TemplateExerciseMapperImplTest {

    private final TemplateExerciseMapperImpl templateExerciseMapper = new TemplateExerciseMapperImpl();
    private final ExerciseDefinitionMapperImpl exerciseDefinitionMapper = new ExerciseDefinitionMapperImpl();

    @Test
    void mapTemplateExerciseIncludesMuscleGroupAndOrderIndex() {
        final ExerciseDefinition exerciseDefinition = new ExerciseDefinition();
        exerciseDefinition.setId(1);
        exerciseDefinition.setName("Bench Press");
        exerciseDefinition.setMuscleGroup("Chest");
        final TemplateExercise templateExercise = new TemplateExercise();
        templateExercise.setExercise(exerciseDefinition);
        templateExercise.setOrderIndex(4);
        templateExercise.setNormalSets(3);
        templateExercise.setFailureSets(1);
        templateExercise.setRestSeconds(75);

        final TemplateExerciseDTO dto = templateExerciseMapper.mapEntityToDto(templateExercise);

        assertEquals(1, dto.exerciseId());
        assertEquals("Bench Press", dto.exerciseName());
        assertEquals("Chest", dto.muscleGroup());
        assertEquals(4, dto.orderIndex());
        assertEquals(3, dto.normalSets());
        assertEquals(1, dto.failureSets());
        assertEquals(75, dto.restSeconds());
    }

    @Test
    void mapExerciseDefinitionIncludesMuscleGroup() {
        final ExerciseDefinition exerciseDefinition = new ExerciseDefinition();
        exerciseDefinition.setId(8);
        exerciseDefinition.setName("Lat Pulldown");
        exerciseDefinition.setMuscleGroup("Back");

        final TemplateExerciseDTO dto = exerciseDefinitionMapper.mapEntityToDto(exerciseDefinition);

        assertEquals(8, dto.exerciseId());
        assertEquals("Lat Pulldown", dto.exerciseName());
        assertEquals("Back", dto.muscleGroup());
        assertNull(dto.orderIndex());
        assertNull(dto.normalSets());
        assertNull(dto.failureSets());
        assertNull(dto.restSeconds());
    }
}
