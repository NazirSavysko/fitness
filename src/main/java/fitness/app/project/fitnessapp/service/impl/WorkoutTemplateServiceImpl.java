package fitness.app.project.fitnessapp.service.impl;

import fitness.app.project.fitnessapp.dto.TemplateExerciseConfigDTO;
import fitness.app.project.fitnessapp.exception.WorkoutTemplateNotFoundException;
import fitness.app.project.fitnessapp.model.TemplateExercise;
import fitness.app.project.fitnessapp.model.WorkoutTemplate;
import fitness.app.project.fitnessapp.repository.WorkoutTemplateRepository;
import fitness.app.project.fitnessapp.service.ExerciseDefinitionService;
import fitness.app.project.fitnessapp.service.WorkoutTemplateService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
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
    public WorkoutTemplate getWorkoutTemplateById(final Integer templateId, final String email) {
        return this.workoutTemplateRepository.findByIdAndUser_Email(templateId, email)
                .orElseThrow(() -> new WorkoutTemplateNotFoundException(WORKOUT_TEMPLATE_NOT_FOUND_MESSAGE));
    }

    @Override
    public void saveWorkout(final WorkoutTemplate workoutTemplate) {
        this.workoutTemplateRepository.save(workoutTemplate);
    }

    @Override
    public List<TemplateExercise> buildTemplateExercises(final WorkoutTemplate workoutTemplate, final List<TemplateExerciseConfigDTO> exercises) {
        final List<TemplateExercise> templateExercises = new ArrayList<>(exercises.size());

        for (int i = 0; i < exercises.size(); i++) {
            final TemplateExerciseConfigDTO exerciseConfig = exercises.get(i);
            final TemplateExercise templateExercise = new TemplateExercise();
            templateExercise.setTemplate(workoutTemplate);
            templateExercise.setExercise(this.exerciseDefinitionService.getReferenceById(exerciseConfig.exerciseId()));
            templateExercise.setOrderIndex(i);
            templateExercise.setNormalSets(exerciseConfig.normalSets());
            templateExercise.setFailureSets(exerciseConfig.failureSets());
            templateExercises.add(templateExercise);
        }

        return templateExercises;
    }

    @Override
    public void validateScheduledDayConflicts(final String email,
                                              final DayOfWeek scheduledDay,
                                              final Integer templateIdToExclude) {
        if (scheduledDay == null) {
            return;
        }

        final List<WorkoutTemplate> existingTemplates = this.workoutTemplateRepository.findAllByUser_Email(email);
        final boolean hasConflict = existingTemplates.stream()
                .filter(template -> templateIdToExclude == null || !templateIdToExclude.equals(template.getId()))
                .map(WorkoutTemplate::getScheduledDay)
                .anyMatch(scheduledDay::equals);
        if (hasConflict) {
            throw new IllegalArgumentException("Day conflict: You already have a template scheduled for " + scheduledDay + ".");
        }
    }
}
