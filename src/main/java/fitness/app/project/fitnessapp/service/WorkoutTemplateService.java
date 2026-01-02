package fitness.app.project.fitnessapp.service;

import fitness.app.project.fitnessapp.model.WorkoutTemplate;

import java.util.List;

public interface WorkoutTemplateService {
    List<WorkoutTemplate> getTemplatesByUserEmail(String email);

    void deleteTemplateByIdAndUserEmail(Integer templateId, String name);

    WorkoutTemplate getWorkoutTemplateById(Integer templateId, final String email);

    void saveWorkout(WorkoutTemplate workoutTemplate);
}
