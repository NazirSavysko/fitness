CREATE TABLE fitness_app.workout_template_scheduled_day
(
    template_id   INTEGER     NOT NULL REFERENCES fitness_app.workout_template (id) ON DELETE CASCADE,
    scheduled_day VARCHAR(20) NOT NULL,
    PRIMARY KEY (template_id, scheduled_day)
);
