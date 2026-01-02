package fitness.app.project.fitnessapp.service.impl;

import fitness.app.project.fitnessapp.model.ExerciseDefinition;
import fitness.app.project.fitnessapp.repository.ExerciseDefinitionRepository;
import fitness.app.project.fitnessapp.service.ExerciseDefinitionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@AllArgsConstructor
public class ExerciseDefinitionServiceImpl implements ExerciseDefinitionService {
    private final ExerciseDefinitionRepository exerciseDefinitionRepository;

    @Override
    public ExerciseDefinition getReferenceById(final Integer id) {
       return  this.exerciseDefinitionRepository.getReferenceById(id);
    }

    @Override
    public List<ExerciseDefinition> getAllExerciseDefinitions() {
        return this.exerciseDefinitionRepository.findAll();
    }


}
