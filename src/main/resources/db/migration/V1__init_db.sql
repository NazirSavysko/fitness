CREATE SCHEMA fitness_app;

CREATE TABLE fitness_app.auth
(
    auth_id       INTEGER PRIMARY KEY,
    email         VARCHAR(50),
    role          VARCHAR(10),
    password_hash VARCHAR(255),
    created_at    TIMESTAMP
);

CREATE TABLE fitness_app.fitness_user
(
    user_id INTEGER PRIMARY KEY,
    name    VARCHAR(20),
    surname VARCHAR(30),
    auth_id INTEGER
);

CREATE TABLE fitness_app.exercise_definition
(
    exercise_def_id INTEGER PRIMARY KEY,
    name            VARCHAR(100)
);

CREATE TABLE fitness_app.workout_template
(
    template_id INTEGER PRIMARY KEY,
    user_id     INTEGER,
    name        VARCHAR(100)
);

CREATE TABLE fitness_app.template_exercise
(
    template_id     INTEGER,
    exercise_def_id INTEGER
);

CREATE TABLE fitness_app.workout_session
(
    session_id   INTEGER PRIMARY KEY,
    user_id      INTEGER,
    template_id  INTEGER,
    session_date TIMESTAMP
);

CREATE TABLE fitness_app.exercise_log
(
    log_id          INTEGER PRIMARY KEY,
    session_id      INTEGER,
    exercise_def_id INTEGER,
    set_number      INTEGER,
    reps            INTEGER,
    weight          DECIMAL(6, 2),
    rest_seconds    INTEGER
);