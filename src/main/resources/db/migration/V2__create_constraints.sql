ALTER TABLE fitness_app.users
    ALTER COLUMN email SET NOT NULL,
ALTER COLUMN password_hash SET NOT NULL,
    ADD CONSTRAINT users_email_uk UNIQUE (email);

ALTER TABLE fitness_app.exercise_definition
    ALTER COLUMN name SET NOT NULL,
    ADD CONSTRAINT exercise_definition_name_uk UNIQUE (name);

ALTER TABLE fitness_app.workout_template
    ADD CONSTRAINT fk_workout_template_user
        FOREIGN KEY (user_id) REFERENCES fitness_app.users (id)
            ON DELETE CASCADE;

ALTER TABLE fitness_app.template_exercise
    ALTER COLUMN template_id SET NOT NULL,
ALTER COLUMN exercise_id SET NOT NULL,

    ADD CONSTRAINT fk_template_exercise_template
        FOREIGN KEY (template_id) REFERENCES fitness_app.workout_template (id)
            ON DELETE CASCADE,

    ADD CONSTRAINT fk_template_exercise_exercise
        FOREIGN KEY (exercise_id) REFERENCES fitness_app.exercise_definition (id)
            ON DELETE CASCADE;

-- 5. Связи для Сессий (Workout Session)
ALTER TABLE fitness_app.workout_session
    ALTER COLUMN started_at SET NOT NULL,

    ADD CONSTRAINT fk_workout_session_user
        FOREIGN KEY (user_id) REFERENCES fitness_app.users (id)
            ON DELETE CASCADE,

    ADD CONSTRAINT fk_workout_session_template
        FOREIGN KEY (source_template_id) REFERENCES fitness_app.workout_template (id)
            ON DELETE SET NULL;

ALTER TABLE fitness_app.session_exercise
    ALTER COLUMN session_id SET NOT NULL,
ALTER COLUMN exercise_id SET NOT NULL,

    ADD CONSTRAINT fk_session_exercise_session
        FOREIGN KEY (session_id) REFERENCES fitness_app.workout_session (id)
            ON DELETE CASCADE,

    ADD CONSTRAINT fk_session_exercise_definition
        FOREIGN KEY (exercise_id) REFERENCES fitness_app.exercise_definition (id)
            ON DELETE RESTRICT;

ALTER TABLE fitness_app.exercise_set
    ALTER COLUMN session_exercise_id SET NOT NULL,
ALTER COLUMN set_number SET NOT NULL,

    ADD CONSTRAINT fk_exercise_set_session_exercise
        FOREIGN KEY (session_exercise_id) REFERENCES fitness_app.session_exercise (id)
            ON DELETE CASCADE; -- Удалили упражнение из сессии -> удалились подходы