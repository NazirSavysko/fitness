package fitness.app.project.fitnessapp.repository;

import fitness.app.project.fitnessapp.model.SessionExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionExerciseRepository extends JpaRepository<SessionExercise, Integer> {

    Optional<SessionExercise> findByIdAndSession_User_Email(Integer id, String userEmail);
}
