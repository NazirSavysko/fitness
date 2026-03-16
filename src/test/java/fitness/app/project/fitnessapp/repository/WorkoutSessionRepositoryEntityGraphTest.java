package fitness.app.project.fitnessapp.repository;

import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.repository.EntityGraph;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkoutSessionRepositoryEntityGraphTest {

    @Test
    void dashboardDateRangeQueryAvoidsBagCollisionsInFetchGraph() throws NoSuchMethodException {
        final Method method = WorkoutSessionRepository.class.getMethod(
                "findAllByUser_EmailAndStartedAtBetweenOrderByStartedAtAsc",
                String.class,
                java.time.LocalDateTime.class,
                java.time.LocalDateTime.class
        );
        final EntityGraph entityGraph = method.getAnnotation(EntityGraph.class);

        assertNotNull(entityGraph, "Expected EntityGraph annotation on dashboard date range query");

        final List<String> attributePaths = List.of(entityGraph.attributePaths());
        assertTrue(attributePaths.contains("exercises"),
                "Dashboard date range query should still prefetch session exercises");
        assertTrue(attributePaths.contains("sourceTemplate"),
                "Dashboard date range query should still prefetch source template");
        assertFalse(attributePaths.contains("exercises.sets"),
                "Dashboard date range query must not prefetch exercises.sets to avoid MultipleBagFetchException");
        assertFalse(attributePaths.contains("sourceTemplate.exercises"),
                "Dashboard date range query must not prefetch sourceTemplate.exercises to avoid MultipleBagFetchException");
    }
}
