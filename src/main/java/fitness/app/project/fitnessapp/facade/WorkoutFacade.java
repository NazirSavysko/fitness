package fitness.app.project.fitnessapp.facade;

import fitness.app.project.fitnessapp.dto.WorkoutSessionDTO;

public interface WorkoutFacade {

    WorkoutSessionDTO startWorkout(Integer templateId, String email);
}
