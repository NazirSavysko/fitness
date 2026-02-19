package fitness.app.project.fitnessapp.repository;

import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkoutTemplateRepository extends JpaRepository<WorkoutTemplate, Integer> {
    List<WorkoutTemplate> findAllByUser_Email(String email);

    boolean existsByUser_Email(String email);
}
