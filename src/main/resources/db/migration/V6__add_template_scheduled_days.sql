CREATE TABLE fitness_app.workout_template_scheduled_days
(
    workout_template_id INTEGER     NOT NULL REFERENCES fitness_app.workout_template (id) ON DELETE CASCADE,
    scheduled_days      VARCHAR(16) NOT NULL
);

CREATE INDEX idx_template_scheduled_days_template_id
    ON fitness_app.workout_template_scheduled_days (workout_template_id);
