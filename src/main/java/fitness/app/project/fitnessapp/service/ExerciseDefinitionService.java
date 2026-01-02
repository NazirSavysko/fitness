package fitness.app.project.fitnessapp.service;

import fitness.app.project.fitnessapp.model.ExerciseDefinition;

import java.util.List;

public interface ExerciseDefinitionService {

   ExerciseDefinition getReferenceById(Integer id);

   List<ExerciseDefinition> getAllExerciseDefinitions();
}
