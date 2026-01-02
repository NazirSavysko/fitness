package fitness.app.project.fitnessapp.service.impl;

import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import fitness.app.project.fitnessapp.repository.WorkoutTemplateRepository;
import fitness.app.project.fitnessapp.service.WorkoutTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public final class WorkoutTemplateServiceImpl implements WorkoutTemplateService {
    private final WorkoutTemplateRepository workoutTemplateRepository;

    @Override
    public List<WorkoutTemplate> getTemplatesByUserEmail(final String email) {
        return this.workoutTemplateRepository.findAllByUser_UserDetails_Email(email);
    }
}
