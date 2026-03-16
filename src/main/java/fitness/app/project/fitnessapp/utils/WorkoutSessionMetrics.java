package fitness.app.project.fitnessapp.utils;

import fitness.app.project.fitnessapp.model.SessionExercise;
import fitness.app.project.fitnessapp.model.TemplateExercise;
import fitness.app.project.fitnessapp.model.WorkoutSession;

import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public final class WorkoutSessionMetrics {
    private WorkoutSessionMetrics() {}

    public static long calculateDurationInMinutes(final WorkoutSession workoutSession) {
        if (workoutSession.getEndedAt() == null) {
            return 0;
        }
        return Duration.between(workoutSession.getStartedAt(), workoutSession.getEndedAt()).toMinutes();
    }

    public static int calculateTotalSets(final WorkoutSession workoutSession) {
        if (workoutSession.getExercises() == null) {
            return 0;
        }
        return workoutSession.getExercises().stream()
                .mapToInt(sessionExercise -> sessionExercise.getSets() == null ? 0 : sessionExercise.getSets().size())
                .sum();
    }

    public static String calculateMuscleGroups(final WorkoutSession workoutSession) {
        if (workoutSession.getExercises() == null) {
            return "";
        }
        return String.join(", ", workoutSession.getExercises().stream()
                .map(SessionExercise::getExercise)
                .filter(exerciseDefinition -> exerciseDefinition != null && exerciseDefinition.getMuscleGroup() != null)
                .map(exerciseDefinition -> exerciseDefinition.getMuscleGroup().trim())
                .filter(muscleGroup -> !muscleGroup.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    public static int calculateCompletionPercentage(final WorkoutSession workoutSession) {
        if (workoutSession.getSourceTemplate() == null || workoutSession.getSourceTemplate().getExercises() == null) {
            return 0;
        }
        final int expectedSets = workoutSession.getSourceTemplate().getExercises().stream()
                .mapToInt(WorkoutSessionMetrics::countExpectedSets)
                .sum();
        if (expectedSets <= 0 || workoutSession.getExercises() == null) {
            return 0;
        }
        final long completedSets = workoutSession.getExercises().stream()
                .map(SessionExercise::getSets)
                .filter(sets -> sets != null)
                .flatMap(List::stream)
                .filter(set -> set.getWeight() != null && set.getReps() != null)
                .count();
        return (int) ((completedSets * 100) / expectedSets);
    }

    private static int countExpectedSets(final TemplateExercise templateExercise) {
        final int normalSets = templateExercise.getNormalSets() == null ? 0 : templateExercise.getNormalSets();
        final int failureSets = templateExercise.getFailureSets() == null ? 0 : templateExercise.getFailureSets();
        return normalSets + failureSets;
    }
}
