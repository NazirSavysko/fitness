package fitness.app.project.fitnessapp.repository;

import fitness.app.project.fitnessapp.model.ExerciseSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseSetRepository extends JpaRepository<ExerciseSet, Integer> {

    Optional<ExerciseSet> findTopBySessionExercise_IdOrderBySetNumberDesc(Integer sessionExerciseId);

    Optional<ExerciseSet> findByIdAndSessionExercise_Session_User_Email(Integer id, String userEmail);
}
