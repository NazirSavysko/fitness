package fitness.app.project.fitnessapp.repository;

import fitness.app.project.fitnessapp.model.WorkoutSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Integer>, JpaSpecificationExecutor<WorkoutSession> {

    Page<WorkoutSession> findAllByUser_EmailAndEndedAtIsNotNullOrderByStartedAtDesc(String userEmail, Pageable pageable);
    Optional<WorkoutSession> findFirstByUser_EmailAndEndedAtIsNull(String email);
    boolean existsByUser_EmailAndStartedAtBetween(String email, LocalDateTime startOfDay, LocalDateTime endOfDay);

    @EntityGraph(attributePaths = {"sourceTemplate", "sourceTemplate.exercises", "exercises", "exercises.exercise", "exercises.sets"})
    List<WorkoutSession> findAllByUser_EmailAndStartedAtBetweenOrderByStartedAtAsc(String userEmail, LocalDateTime start, LocalDateTime end);

    @EntityGraph(attributePaths = {"sourceTemplate", "exercises", "exercises.exercise"})
    Optional<WorkoutSession> findByIdAndUser_Email(Integer id, String userEmail);
}
