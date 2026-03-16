package fitness.app.project.fitnessapp.service;

import fitness.app.project.fitnessapp.dto.TemplateExerciseConfigDTO;
import fitness.app.project.fitnessapp.model.TemplateExercise;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;

import java.time.DayOfWeek;
import java.util.List;

public interface WorkoutTemplateService {
    List<WorkoutTemplate> getTemplatesByUserEmail(String email);

    void deleteTemplateByIdAndUserEmail(Integer templateId, String name);

    WorkoutTemplate getWorkoutTemplateById(Integer templateId, final String email);

    void saveWorkout(WorkoutTemplate workoutTemplate);

    List<TemplateExercise> buildTemplateExercises(WorkoutTemplate workoutTemplate, List<TemplateExerciseConfigDTO> exercises);

    void validateScheduledDayConflicts(String email, DayOfWeek scheduledDay, Integer templateIdToExclude);
}
