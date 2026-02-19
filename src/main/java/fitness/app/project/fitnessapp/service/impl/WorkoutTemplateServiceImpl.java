package fitness.app.project.fitnessapp.service.impl;

import fitness.app.project.fitnessapp.exception.WorkoutTemplateNotFoundException;
import fitness.app.project.fitnessapp.model.TemplateExercise;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import fitness.app.project.fitnessapp.repository.WorkoutTemplateRepository;
import fitness.app.project.fitnessapp.service.ExerciseDefinitionService;
import fitness.app.project.fitnessapp.service.WorkoutTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public final class WorkoutTemplateServiceImpl implements WorkoutTemplateService {
    private static final String WORKOUT_TEMPLATE_NOT_FOUND_MESSAGE = "Workout template with this id not found";

    private final WorkoutTemplateRepository workoutTemplateRepository;
    private final ExerciseDefinitionService exerciseDefinitionService;

    @Override
    public List<WorkoutTemplate> getTemplatesByUserEmail(final String email) {
        return this.workoutTemplateRepository.findAllByUser_Email(email);
    }

    @Override
    @PreAuthorize("@workoutTemplateRepository.existsByUser_Email(#email)")
    public void deleteTemplateByIdAndUserEmail(final Integer templateId, final String email) {
        if (this.workoutTemplateRepository.findById(templateId).isEmpty()) {
            throw new WorkoutTemplateNotFoundException(WORKOUT_TEMPLATE_NOT_FOUND_MESSAGE);
        }

        this.workoutTemplateRepository.deleteById(templateId);
    }

    @Override
    @PreAuthorize("@workoutTemplateRepository.existsByUser_Email(#email)")
    public WorkoutTemplate getWorkoutTemplateById(final Integer templateId, final String email) {
        return this.workoutTemplateRepository.findById(templateId)
                .orElseThrow(() -> new WorkoutTemplateNotFoundException(WORKOUT_TEMPLATE_NOT_FOUND_MESSAGE));
    }

    @Override
    public void saveWorkout(final WorkoutTemplate workoutTemplate) {
        this.workoutTemplateRepository.save(workoutTemplate);
    }

    @Override
    public List<TemplateExercise> buildTemplateExercises(final WorkoutTemplate workoutTemplate, final List<Integer> exerciseIds) {
        final List<TemplateExercise> templateExercises = new ArrayList<>(exerciseIds.size());

        for (int i = 0; i < exerciseIds.size(); i++) {
            final TemplateExercise templateExercise = new TemplateExercise();
            templateExercise.setTemplate(workoutTemplate);
            templateExercise.setExercise(this.exerciseDefinitionService.getReferenceById(exerciseIds.get(i)));
            templateExercise.setOrderIndex(i);
            templateExercises.add(templateExercise);
        }

        return templateExercises;
    }
}
