package fitness.app.project.fitnessapp.repository;

import fitness.app.project.fitnessapp.model.ExerciseDefinition;
import jakarta.persistence.QueryHint;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

import java.util.List;

public interface ExerciseDefinitionRepository extends JpaRepository<ExerciseDefinition, Integer> {
    @Override
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    @NonNull
    List<ExerciseDefinition> findAll();
}
