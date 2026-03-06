ALTER TABLE fitness_app.template_exercise
    ADD COLUMN normal_sets INTEGER NOT NULL DEFAULT 0;

ALTER TABLE fitness_app.template_exercise
    ADD COLUMN failure_sets INTEGER NOT NULL DEFAULT 0;

ALTER TABLE fitness_app.template_exercise
    ADD COLUMN rest_seconds INTEGER NOT NULL DEFAULT 60;
