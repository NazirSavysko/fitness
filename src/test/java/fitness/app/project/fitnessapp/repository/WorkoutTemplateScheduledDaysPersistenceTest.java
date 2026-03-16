package fitness.app.project.fitnessapp.repository;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkoutTemplateScheduledDaysPersistenceTest {

    @Test
    void scheduledDaysJoinTableMigrationExists() throws IOException {
        final String migrationSql = Files.readString(Path.of(
                "src/main/resources/db/migration/V6__create_workout_template_scheduled_days.sql"
        ));

        assertTrue(migrationSql.contains("CREATE TABLE fitness_app.workout_template_scheduled_day"));
        assertTrue(migrationSql.contains("PRIMARY KEY (template_id, scheduled_day)"));
    }
}
