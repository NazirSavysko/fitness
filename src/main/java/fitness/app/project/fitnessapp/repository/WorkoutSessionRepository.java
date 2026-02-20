package fitness.app.project.fitnessapp.repository;

import fitness.app.project.fitnessapp.model.WorkoutSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Integer> {

    Page<WorkoutSession> findAllByUser_EmailAndEndedAtIsNotNullOrderByStartedAtDesc(String userEmail, Pageable pageable);

    @EntityGraph(attributePaths = {"sourceTemplate", "exercises", "exercises.exercise", "exercises.sets"})
    Optional<WorkoutSession> findByIdAndUser_Email(Integer id, String userEmail);
}
