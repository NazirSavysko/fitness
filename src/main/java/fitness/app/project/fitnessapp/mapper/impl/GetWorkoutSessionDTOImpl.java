package fitness.app.project.fitnessapp.mapper.impl;

import fitness.app.project.fitnessapp.dto.WorkoutSessionDTO;
import fitness.app.project.fitnessapp.mapper.GetWorkoutSessionDTO;
import fitness.app.project.fitnessapp.model.WorkoutSession;

public class GetWorkoutSessionDTOImpl implements GetWorkoutSessionDTO {
    @Override
    public WorkoutSessionDTO mapEntityToDto(final WorkoutSession workoutSession) {
        return new WorkoutSessionDTO(

        );
    }
}
