package fitness.app.project.fitnessapp.service;

import fitness.app.project.fitnessapp.dto.AddSetDTO;
import fitness.app.project.fitnessapp.model.WorkoutSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkoutSessionService {

    Integer startWorkout(Integer templateId, String userEmail);

    Integer addSetToExercise(AddSetDTO addSetDTO, String userEmail);

    void finishWorkout(Integer sessionId, String userEmail);

    Page<WorkoutSession> getHistory(String userEmail, Long templateId, String dateRange, String sortBy, Pageable pageable);

    WorkoutSession getWorkoutDetails(Integer sessionId, String userEmail);
}
