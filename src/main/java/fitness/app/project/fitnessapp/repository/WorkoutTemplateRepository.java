package fitness.app.project.fitnessapp.repository;

import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;
import java.util.Optional;

public interface WorkoutTemplateRepository extends JpaRepository<WorkoutTemplate, Integer> {
    List<WorkoutTemplate> findAllByUser_Email(String userEmail);

    boolean existsByUser_Email(String userEmail);

    @EntityGraph(attributePaths = {"exercises", "exercises.exercise"})
    Optional<WorkoutTemplate> findByIdAndUser_Email(Integer id, String userEmail);
}
