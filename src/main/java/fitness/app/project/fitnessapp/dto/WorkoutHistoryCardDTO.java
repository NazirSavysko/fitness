package fitness.app.project.fitnessapp.dto;

public record WorkoutHistoryCardDTO(
        Integer sessionId,
        String name,
        String date,
        long durationMinutes,
        int totalSets,
        String muscleGroups
) {
}
