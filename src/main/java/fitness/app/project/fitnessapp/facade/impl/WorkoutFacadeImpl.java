package fitness.app.project.fitnessapp.facade.impl;

import fitness.app.project.fitnessapp.dto.WorkoutSessionDTO;
import fitness.app.project.fitnessapp.facade.WorkoutFacade;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import fitness.app.project.fitnessapp.service.WorkoutTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public final class WorkoutFacadeImpl implements WorkoutFacade {

   private final WorkoutTemplateService workoutTemplateService;

    @Override
    public WorkoutSessionDTO startWorkout(final Integer templateId, final String email) {
        final WorkoutTemplate workoutTemplate = this.workoutTemplateService.getWorkoutTemplateById(templateId, email);

        return new WorkoutSessionDTO(
                null,
                workoutTemplate.getId(),
                null,
                null,
                List.of()
        );
    }
}
