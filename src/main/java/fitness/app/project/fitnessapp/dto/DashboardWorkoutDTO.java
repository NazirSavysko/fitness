package fitness.app.project.fitnessapp.dto;

import java.time.LocalDate;

public record DashboardWorkoutDTO(
        Integer sessionId,
        String name,
        LocalDate workoutDate,
        String date,
        long durationMinutes,
        int totalSets,
        String muscleGroups,
        Integer completionPercentage,
        boolean active
) {
}
