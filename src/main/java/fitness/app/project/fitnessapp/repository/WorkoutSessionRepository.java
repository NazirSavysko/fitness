package fitness.app.project.fitnessapp.repository;

import fitness.app.project.fitnessapp.model.WorkoutSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.time.LocalDateTime;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Integer>, JpaSpecificationExecutor<WorkoutSession> {

    Page<WorkoutSession> findAllByUser_EmailAndEndedAtIsNotNullOrderByStartedAtDesc(String userEmail, Pageable pageable);
    Optional<WorkoutSession> findFirstByUser_EmailAndEndedAtIsNull(String email);
    boolean existsByUser_EmailAndStartedAtBetween(String email, LocalDateTime startOfDay, LocalDateTime endOfDay);

    @EntityGraph(attributePaths = {"sourceTemplate", "exercises", "exercises.exercise"})
    Optional<WorkoutSession> findByIdAndUser_Email(Integer id, String userEmail);
}
