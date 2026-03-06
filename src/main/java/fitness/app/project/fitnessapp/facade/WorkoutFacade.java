package fitness.app.project.fitnessapp.facade;

import fitness.app.project.fitnessapp.dto.ActiveWorkoutDTO;
import fitness.app.project.fitnessapp.dto.AddSetDTO;
import fitness.app.project.fitnessapp.dto.UpdateExerciseSetDTO;
import fitness.app.project.fitnessapp.dto.WorkoutHistoryCardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkoutFacade {

    Integer startWorkout(Integer templateId, String email);

    Integer addSetToExercise(AddSetDTO addSetDTO, String email);

    Integer updateExerciseSet(UpdateExerciseSetDTO updateExerciseSetDTO, String email);

    void finishWorkout(Integer sessionId, String email);

    Page<WorkoutHistoryCardDTO> getHistory(String email, Long templateId, String dateRange, String sortBy, Pageable pageable);

    ActiveWorkoutDTO getWorkoutDetails(Integer sessionId, String email);
}
