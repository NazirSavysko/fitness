package fitness.app.project.fitnessapp.dto;

public record WorkoutHistoryCardDTO(
        Integer sessionId,
        String name,
        String date,
        String dateKey,
        long durationMinutes,
        int totalSets,
        String muscleGroups,
        Integer completionPercentage
) {
}
