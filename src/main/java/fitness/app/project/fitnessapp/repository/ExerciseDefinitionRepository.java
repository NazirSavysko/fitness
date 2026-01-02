package fitness.app.project.fitnessapp.repository;

import fitness.app.project.fitnessapp.model.ExerciseDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseDefinitionRepository extends JpaRepository<ExerciseDefinition, Integer> {
}
