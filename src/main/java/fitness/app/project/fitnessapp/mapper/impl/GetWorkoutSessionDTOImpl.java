package fitness.app.project.fitnessapp.mapper.impl;

import fitness.app.project.fitnessapp.dto.WorkoutSessionDTO;
import fitness.app.project.fitnessapp.mapper.GetWorkoutSessionDTO;
import fitness.app.project.fitnessapp.model.WorkoutSession;

import java.util.List;

public class GetWorkoutSessionDTOImpl implements GetWorkoutSessionDTO {
    @Override
    public WorkoutSessionDTO mapEntityToDto(final WorkoutSession workoutSession) {
        return new WorkoutSessionDTO(
                workoutSession.getId(),
                workoutSession.getSourceTemplate() != null ? workoutSession.getSourceTemplate().getId() : null,
                workoutSession.getStartedAt(),
                workoutSession.getEndedAt(),
                List.of()
        );
    }
}
